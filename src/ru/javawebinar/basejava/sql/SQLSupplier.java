package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.model.Resume;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLSupplier {
    Resume get() throws SQLException;
}
