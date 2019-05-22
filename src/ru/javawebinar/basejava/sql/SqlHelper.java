package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T setQuery(String query, SQLFunction<T> operation) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return operation.apply(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public <T> T setTransaction(SQLTransactionFunction<T> operation) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T result = operation.apply(conn);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public void setPreparedStatement(String query, Connection conn, SQLConsumer operation) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            operation.accept(ps);
        }
    }

    @FunctionalInterface
    public interface SQLFunction<T> {
        T apply(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLTransactionFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

