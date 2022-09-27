package project.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionCreator {
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "Password12345";
    private static final String URL = "jdbc:postgresql://localhost/book_library_project";

    public ConnectionCreator() {
    }

    public static Connection createConnection() {
        try {
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to connect DB: " + e.getLocalizedMessage());
            System.exit(1);
        }
        return null;
    }
}
