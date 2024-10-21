package dev.personalizednewsrecsystem;
import com.mongodb.MongoException;
import javafx.collections.ObservableList;

import java.sql.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseHandler {
    private String sqlUrl = "jdbc:mysql://localhost:3306/OOD_CW";
    private String sqlUsername = "root";
    private String sqlPassword = "chira@root";
    private Connection connection;
    private String sqlDriver = "com.mysql.cj.jdbc.Driver";
    private String mongoUri = "mongodb://localhost:27017";
    private String mongodbName = "OOD_CW";
    private String mongoCollection = "articles";

    public DatabaseHandler() {
        try {
            // Loading driver
            Class.forName(this.sqlDriver);
            // Connection
            connection = DriverManager.getConnection(this.sqlUrl, this.sqlUsername, this.sqlPassword);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            MongoClient mongoClient = MongoClients.create(mongoUri);
            MongoDatabase database = mongoClient.getDatabase(mongodbName);
            MongoCollection<Document> collection = database.getCollection(mongoCollection);
        } catch (MongoException e) {
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
                String sqlQuery = "SELECT COUNT(*) FROM user WHERE email = '" + email + "'";
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
        return false;
    }

    public boolean adminCheck(String email) {
        if (connection != null) {
            String sqlQuery = "SELECT admin FROM user WHERE email = '" + email + "'";
            try{
                Statement statement = this.connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlQuery);

                if (rs.next()) {
                    int isAdmin = rs.getInt("admin");
                    return isAdmin == 1;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
