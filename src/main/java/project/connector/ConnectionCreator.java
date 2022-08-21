package project.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionCreator {
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "Password12345";
    private static final String URL = "jdbc:postgresql://localhost/book_library_project";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    public ConnectionCreator() {
    }

    public static Connection createConnection() throws SQLException {
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
