package ru.tricky_compression.database;

import java.sql.*;

public class ConnectToPostgres {
    private static final String url = "jdbc:postgresql://51.250.23.237:5432/";
    private static final String user = "admin";
    private static final String password = "admin";
    private static final String tableName = "chunks";

    public static void main(String[] args) {
        String hash = "someHash";
        boolean result;
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement selectStatement = connection.createStatement();
        ) {
            ResultSet results = selectStatement.executeQuery(
                    "SELECT 1 FROM " + tableName + " WHERE hash = '" + hash + "';"
            );
            result = results.next();
        } catch (SQLException ignored) {
            result = false;
        }
        System.out.println(result);
    }
}