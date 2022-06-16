package ru.tricky_compression.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String url = "jdbc:postgresql://51.250.23.237:5432/";
    private static final String user = "admin";
    private static final String password = "admin";
    private static final String tableName = "chunks";

    public DataBase() {
        // DEBUG BEGIN
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement selectStatement = connection.createStatement();
        ) {
            ResultSet results = selectStatement.executeQuery(
                    "SELECT * FROM " + tableName + ";"
            );
            while (results.next()) {
                System.out.printf("%d. %s \t %s\n",
                        results.getRow(),
                        results.getString("hash"),
                        results.getString("path"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
        // DEBUG END
    }

    public boolean contains(String hash) {
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement selectStatement = connection.createStatement();
        ) {
            ResultSet results = selectStatement.executeQuery(
                    "SELECT COUNT(1) FROM " + tableName + " WHERE hash = " + hash + ";"
            );
            return results.next();
        } catch (SQLException ignored) {
            return false;
        }
    }
}
