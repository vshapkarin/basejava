package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T> {
    T apply(PreparedStatement ps) throws SQLException;
}