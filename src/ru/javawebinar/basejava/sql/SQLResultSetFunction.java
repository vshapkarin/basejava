package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.model.Resume;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLResultSetFunction {
    Resume get(ResultSet rs) throws SQLException;
}
