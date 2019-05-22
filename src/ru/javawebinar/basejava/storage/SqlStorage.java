package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

                    if(rs.getString("type") != null) {
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
                        if (ps.executeUpdate() != 0) {
                            try (PreparedStatement ps2 = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                                insertContacts(ps2, resume);
                            }
                        }
                    }
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
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                        insertContacts(ps, resume);
                    }
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
            try (PreparedStatement psResumes = conn.prepareStatement("SELECT * FROM resume ORDER BY full_Name, uuid");
                 PreparedStatement psContacts = conn.prepareStatement("SELECT resume_uuid, type, value FROM contact WHERE resume_uuid=?")) {
                List<Resume> resumes = new ArrayList<>();
                ResultSet rsResumes = psResumes.executeQuery();

                ResultSet rsContacts;
                String uuid;
                Resume resume;

                while (rsResumes.next()) {
                    uuid = rsResumes.getString("uuid");
                    resume = new Resume(uuid.trim(), rsResumes.getString("full_name").trim());

                    psContacts.setString(1, uuid);
                    rsContacts = psContacts.executeQuery();
                    while (rsContacts.next()) {
                        resume.addContact(ContactType.valueOf(rsContacts.getString("type")), rsContacts.getString("value"));
                    }

                    resumes.add(resume);
                }
                return resumes;
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

    private void insertContacts(PreparedStatement ps, Resume resume) throws SQLException {
        for (Map.Entry<ContactType, Contact> e : resume.getContacts().entrySet()) {
            ps.setString(1, resume.getUuid());
            ps.setString(2, e.getKey().name());
            ps.setString(3, e.getValue().toString());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}