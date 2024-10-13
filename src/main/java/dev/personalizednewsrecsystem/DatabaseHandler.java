package dev.personalizednewsrecsystem;
import java.sql.*;

public class DatabaseHandler {
    private String url = "jdbc:mysql://localhost:3306/OOD_CW";
    private String username = "root";
    private String password = "chira@root";
    private Connection connection;
    private String driver = "com.mysql.cj.jdbc.Driver";
    public DatabaseHandler() {
        try {
            // Loading driver
            Class.forName(this.driver);
            // Connection
            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResultSet fetchUser(String sqlQuery) {
        if (connection != null) {
            try {
                Statement statement = this.connection.createStatement();
                return statement.executeQuery(sqlQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
