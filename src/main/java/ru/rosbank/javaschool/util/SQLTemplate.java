package ru.rosbank.javaschool.util;


import ru.rosbank.javaschool.model.Manager;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class SQLTemplate {
    public <T> List<T> queryForList(String url, String query, RowMapper<T> mapper) throws SQLException {
        return execute(url, query, resultSet -> {
            List<T> list = new LinkedList<>();
            while (resultSet.next()) {
                list.add(mapper.map(resultSet));
            }
            return list;
        });
    }

    public <T> Optional<T> queryForObject(String url, String query, RowMapper<T> mapper) throws SQLException {
        return execute(url, query, resultSet -> {
            if (resultSet.next()) {
                return Optional.of(mapper.map(resultSet));
            }
            return Optional.empty();
        });
    }

    private <T> T execute(String url, String query, Executable<T> function) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
        ) {
            return function.execute(resultSet);
        }
    }
}
