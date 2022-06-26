package ru.tricky_compression.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String url = "jdbc:postgresql://51.250.23.237:5432/";
    private static final String user = "admin";
    private static final String password = "admin";
    private static final String chunksTable = "chunks";
    private static final String chunksTableColumn = "hash";
    private static final String filesTable = "files";
    private static final String filesTableColumn = "filename";

    public DataBase() {
        System.out.println(System.getProperties());
    }

    private static boolean haveRecordInTable(String table, String column, String record) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement selectStatement = connection.createStatement()) {
            ResultSet results = selectStatement.executeQuery(
                    String.format("SELECT 1 FROM %s WHERE %s = '%s';", table, column, record)
            );
            return results.next();
        } catch (SQLException ignored) {
            return false;
        }
    }

    public static boolean containsHash(String hash) {
        return haveRecordInTable(chunksTable, chunksTableColumn, hash);
    }

    public static void addFilename(String filename) {
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement insertStatement = connection.prepareStatement(
                        String.format("INSERT INTO %s(%s) VALUES ('%s');", filesTable, filesTableColumn, filename)
                )
        ) {
            if (!haveFilename(filename)) {
                int result = insertStatement.executeUpdate();
                if (result != 1) {
                    throw new RuntimeException("Postgres: cannot add filename");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static boolean haveFilename(String filename) {
        return haveRecordInTable(filesTable, filesTableColumn, filename);
    }

    public static String[] getFilenames() throws SQLException {
        List<String> filenames = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement selectStatement = connection.createStatement();
        ) {
            ResultSet results = selectStatement.executeQuery(
                    String.format("SELECT * FROM %s;", filesTable)
            );
            while (results.next()) {
                filenames.add(results.getString(filesTableColumn));
            }
        } catch (SQLException exception) {
            throw exception;
        }
        return filenames.toArray(new String[0]);
    }
}
