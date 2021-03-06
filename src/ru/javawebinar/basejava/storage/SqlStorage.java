package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SQLResultSetFunction;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.setQuery("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return helper.setTransaction(conn -> {
            Resume resume;

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid=?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid=?")) {
                ps.setString(1, uuid);
                bindContacts(ps, rs -> resume);
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid=?")) {
                ps.setString(1, uuid);
                bindSections(ps, rs -> resume);
            }

            return resume;
        });
    }

    @Override
    public void update(Resume resume) {
        helper.<Void>setTransaction(conn -> {
                    String uuid = resume.getUuid();
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, uuid);
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                    }
                    deleteGroup("DELETE FROM contact WHERE resume_uuid=?", conn, uuid);
                    deleteGroup("DELETE FROM section WHERE resume_uuid=?", conn, uuid);
                    insertContacts(conn, resume);
                    insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume resume) {
        helper.<Void>setTransaction(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                    }
                    insertContacts(conn, resume);
                    insertSections(conn, resume);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        helper.<Void>setQuery("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.setTransaction(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                String uuid;
                while (rs.next()) {
                    uuid = rs.getString("uuid").trim();
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name").trim()));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                bindContacts(ps, rs -> resumes.get(rs.getString("resume_uuid").trim()));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                bindSections(ps, rs -> resumes.get(rs.getString("resume_uuid").trim()));
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        return helper.setQuery("SELECT COUNT(*) AS rowcount FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("rowcount");
        });
    }

    private void insertContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, Contact> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, JsonParser.write(e.getValue(), AbstractSection.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteGroup(String query, Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private void bindContacts(PreparedStatement ps, SQLResultSetFunction resumeSupplier) throws SQLException {
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            do {
                resumeSupplier.get(rs).addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
            } while (rs.next());
        }
    }

    private void bindSections(PreparedStatement ps, SQLResultSetFunction resumeSupplier) throws SQLException {
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            do {
                SectionType sectionType = SectionType.valueOf(rs.getString("type"));
                Resume resume = resumeSupplier.get(rs);
                resume.addSection(sectionType, JsonParser.read(rs.getString("content"), AbstractSection.class));
            } while (rs.next());
        }

    }
}