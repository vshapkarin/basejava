package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

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
        return helper.setQuery("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));

                    if (rs.getString("type") != null) {
                        do {
                            resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                        } while (rs.next());
                    }

                    return resume;
                });
    }

    @Override
    public void update(Resume resume) {
        helper.<Void>setTransaction(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
                        ps.setString(1, resume.getFullName());
                        ps.setString(2, resume.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(resume.getUuid());
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
                        ps.setString(1, resume.getUuid());
                        ps.execute();
                    }
                    insertContacts(conn, resume);
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
            try (PreparedStatement psResume = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                Map<String, Resume> resumes = new LinkedHashMap<>();
                ResultSet rsResume = psResume.executeQuery();
                String uuid;
                while (rsResume.next()) {
                    uuid = rsResume.getString("uuid").trim();
                    resumes.put(uuid, new Resume(uuid, rsResume.getString("full_name").trim()));
                }

                try (PreparedStatement psContact = conn.prepareStatement("SELECT * FROM contact c")) {
                    ResultSet rsContact = psContact.executeQuery();
                    if (rsContact.next()) {
                        while (!rsContact.isAfterLast()) {
                            resumes.get(rsContact.getString("resume_uuid").trim())
                                    .addContact(ContactType.valueOf(rsContact.getString("type")), rsContact.getString("value"));
                            rsContact.next();
                        }
                    }
                }
                return new ArrayList<>(resumes.values());
            }
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
}