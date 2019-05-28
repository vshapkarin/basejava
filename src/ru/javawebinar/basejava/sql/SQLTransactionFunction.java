package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLTransactionFunction<T> {
    T apply(Connection conn) throws SQLException;
}