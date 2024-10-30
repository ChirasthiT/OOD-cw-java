package dev.personalizednewsrecsystem;
import com.mongodb.MongoException;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DatabaseHandler {
    private String sqlUrl = "jdbc:mysql://localhost:3306/OOD_CW";
    private String sqlUsername = "root";
    private String sqlPassword = "chira@root";
    private Connection connection;
    private String sqlDriver = "com.mysql.cj.jdbc.Driver";
    private String mongoUri = "mongodb://localhost:27017";
    private String mongodbName = "OOD_CW";
    private String mongoCollection = "articles";
    MongoCollection<Document> collection;

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
            collection = database.getCollection(mongoCollection);
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

    public String getUserPreferences(String email) {
        String sqlQuery = "SELECT user_pref FROM preferences WHERE email = '" + email + "'";
        String preferences = null;

        if (connection != null) {
            try {
                Statement statement = this.connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlQuery);
                while (rs.next()) {
                    preferences = rs.getString("user_pref");
                }
            } catch (SQLException ignored) {
            }
        }
        System.out.println(preferences);
        return preferences;
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
        System.out.println(email);
        if (connection != null) {
            String sqlQuery = "SELECT admin FROM user WHERE email = '" + email + "'";
            try{
                Statement statement = this.connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlQuery);
                if (rs.next()) {
                    int isAdmin = rs.getInt("admin");
                    System.out.println("Admin status found: " + isAdmin);
                    return isAdmin == 1;
                } else {
                    System.out.println("No user found with the given email.");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public Article fetchArticle(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Document article = collection.find(new Document("_id", objectId)).first();

            if (article != null) {
                String title = article.getString("title");
                String author = article.getString("author");
                String content = article.getString("content");
                return new Article(id, title, author, content);
            } else {
                System.out.println("Article not found for ID: " + id);
                return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ObjectId format: " + id);
            return null;
        }
    }

    public void addInteraction(String email, String articleId, String type) {
        String sqlQuery = "INSERT INTO history (email, article_id, interaction_type, interaction_count) " +
                "VALUES ('" + email + "', '" + articleId + "', '" + type + "', 1) " +
                "ON DUPLICATE KEY UPDATE interaction_count = interaction_count + 1";
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteArticle(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            long deletedCount = collection.deleteOne(new Document("_id", objectId)).getDeletedCount();
            return deletedCount > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
