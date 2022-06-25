package ru.tricky_compression.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String url = "jdbc:postgresql://51.250.23.237:5432/";
    private static final String user = "admin";
    private static final String password = "admin";
    private static final String chunksTable = "chunks";
    private static final String filesTable = "files";
    private static final String filesTableColumn = "path";

    public DataBase() {
        // DEBUG BEGIN
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement selectStatement = connection.createStatement();
        ) {
            ResultSet results = selectStatement.executeQuery(
                    "SELECT * FROM " + chunksTable + ";"
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

    public static boolean contains(String hash) {
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement selectStatement = connection.createStatement();
        ) {
            ResultSet results = selectStatement.executeQuery(
                    "SELECT 1 FROM " + chunksTable + " WHERE hash = '" + hash + "';"
            );
            return results.next();
        } catch (SQLException ignored) {
            return false;
        }
    }

    public static void addPath(String filename) {
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO " + filesTable + "(" + filesTableColumn + ") VALUES ('" + filename + "');"
                )
        ) {
            int result = insertStatement.executeUpdate();
            System.out.println("result = " + result);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
