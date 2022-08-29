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

    public static Connection createConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
