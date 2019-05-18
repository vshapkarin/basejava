package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private ConnectionFactory connectionFactory;
    private Object container;


    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setQuery(String query, SQLConsumer<PreparedStatement> operation) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            operation.accept(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public Object getContainer() {
        return container;
    }

    public void setContainer(Object container) {
        this.container = container;
    }

    @FunctionalInterface
    public interface SQLConsumer<PreparedStatement> {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

