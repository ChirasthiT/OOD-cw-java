package dev.personalizednewsrecsystem;
import javafx.collections.ObservableList;

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

    public void addUser(String email, String password, String name) {
        if (connection != null) {
            try {
                String sqlQuery = "INSERT INTO user (email, password, username) VALUES ('" + email + "', '" + password + "', '" + name + "')";
                Statement statement = this.connection.createStatement();
                statement.executeUpdate(sqlQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addPreferences(String email, ObservableList<String> selectedPreferences) {
        String preferences = String.join(",", selectedPreferences);
        if (connection != null) {
            try {
                String sqlQuery = "INSERT INTO preferences (email, user_pref) VALUES ('" + email + "', '" + preferences + "')";
                Statement statement = this.connection.createStatement();
                statement.executeUpdate(sqlQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkEmail(String email) {
        if (connection != null) {
            try {
                String sqlQuery = "SELECT COUNT(*) FROM Users WHERE email = '" + email + "'";
                Statement statement = this.connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlQuery);

                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return
    }
}
