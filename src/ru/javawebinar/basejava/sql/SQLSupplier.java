package ru.javawebinar.basejava.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLSupplier<Resume> {
    Resume get() throws SQLException;
}
