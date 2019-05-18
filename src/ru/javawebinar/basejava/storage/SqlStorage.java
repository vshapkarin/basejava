package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.setQuery("DELETE FROM resume", connectionFactory, (SqlHelper.SQLConsumer<PreparedStatement>) PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return (Resume) SqlHelper.setQuery("SELECT * FROM resume r WHERE r.uuid =?", connectionFactory, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void update(Resume resume) {
        SqlHelper.setQuery("UPDATE resume SET full_Name=? WHERE uuid=?", connectionFactory, ps -> {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
        });
    }

    @Override
    public void save(Resume resume) {
        SqlHelper.setQuery("INSERT INTO resume (uuid, full_name) VALUES (?,?)", connectionFactory, ps -> {
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            try {
                ps.execute();
            } catch (SQLException e) {
                throw new ExistStorageException(resume.getUuid());
            }
        });
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.setQuery("DELETE FROM resume WHERE uuid=?", connectionFactory, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Resume> getAllSorted() {
        return (List<Resume>) SqlHelper.setQuery("SELECT * FROM resume ORDER BY full_Name, uuid", connectionFactory, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim()));
            }
            return list;
        });
    }

    @Override
    public int size() {
        return (int) SqlHelper.setQuery("SELECT COUNT(*) AS rowcount FROM resume", connectionFactory, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("rowcount");
        });
    }
}