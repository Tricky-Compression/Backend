package ru.tricky_compression.database;

import java.sql.*;

public class ConnectToPostgres {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://51.250.23.237:5432/";
        try (
                Connection connection = DriverManager.getConnection(url, "admin", "admin");
                Statement selectStatement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT name FROM records WHERE year > ?;"
                )
        ) {
            ResultSet results = selectStatement.executeQuery("SELECT * FROM records;");
            while (results.next()) {
                System.out.printf("%d. %s \t %s\n",
                        results.getRow(),
                        results.getString("artist"),
                        results.getString("name"));
            }

            preparedStatement.setInt(1, 1995);
            ResultSet anotherResults = preparedStatement.executeQuery();
            while (anotherResults.next()) {
                System.out.printf("%d. %s\n",
                        anotherResults.getRow(),
                        anotherResults.getString("name")
                );
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}