package dev.personalizednewsrecsystem;
import com.mongodb.MongoException;
import dev.personalizednewsrecsystem.User;
import dev.personalizednewsrecsystem.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.Alert;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DatabaseHandler {
    private static String sqlUrl = "jdbc:mysql://localhost:3306/OOD_CW";
    private static String sqlUsername = "root";
    private static String sqlPassword = "chira@root";
    private static Connection connection;
    private static String sqlDriver = "com.mysql.cj.jdbc.Driver";
    private static String mongoUri = "mongodb+srv://Cluster61823:25882588@cluster61823.ppkuv.mongodb.net/?retryWrites=true&w=majority&appName=Cluster61823";
    private static String mongodbName = "OOD_CW";
    private static String mongoCollection = "articles";
    protected static MongoCollection<Document> collection;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public DatabaseHandler() {
        try {
            // Loading driver
            Class.forName(sqlDriver);
            // Connection
            connection = DriverManager.getConnection(sqlUrl, sqlUsername, sqlPassword);
            // Mongo Connection
            MongoClient mongoClient = MongoClients.create(mongoUri);
            MongoDatabase database = mongoClient.getDatabase(mongodbName);
            collection = database.getCollection(mongoCollection);
        } catch (ClassNotFoundException | SQLException | MongoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred");
            alert.setContentText("Error connecting the database");
            alert.showAndWait();
        }
    }

    public static ResultSet fetchUser(String email, String pwd) {
        String sqlQuery = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + pwd + "'";

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                return statement.executeQuery(sqlQuery);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error has occurred");
                alert.setContentText("Error connecting the database");
                alert.showAndWait();
            }
        }
        return null;
    }

    public static void addUser(String email, String password, String name) {
        if (connection != null) {
            try {
                String sqlQuery = "UPDATE user SET password = '" + password + "', username = '" + name + "' WHERE email = '" + email + "'";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getUserPreferences(String email) {
        String sqlQuery = "SELECT user_pref FROM preferences WHERE email = '" + email + "'";
        String preferences = null;

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
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

    public static void addPreferences(String email, ObservableList<String> selectedPreferences) {
        String preferences = String.join(",", selectedPreferences);
        if (connection != null) {
            try {
                String sqlQuery = "INSERT INTO preferences (email, user_pref) VALUES ('" + email + "', '" + preferences + "')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkEmail(String email) {
        if (connection != null) {
            try {
                String sqlQuery = "SELECT COUNT(*) FROM user WHERE email = '" + email + "'";
                Statement statement = connection.createStatement();
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

    private static boolean adminCheck(String email) {
        System.out.println(email);
        if (connection != null) {
            String sqlQuery = "SELECT admin FROM user WHERE email = '" + email + "'";
            try{
                Statement statement = connection.createStatement();
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

    // admincheck concurrent
    public static CompletableFuture<Boolean> adminCheckAsync(String email) {
        return CompletableFuture.supplyAsync(() -> adminCheck(email), executorService);
    }

    public static Article fetchArticle(String id) {
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

    public static void addInteraction(String email, String articleId, String type) {
        String sqlQuery = "INSERT INTO history (email, article_id, interaction_type, interaction_count) " +
                "VALUES ('" + email + "', '" + articleId + "', '" + type + "', 1) " +
                "ON DUPLICATE KEY UPDATE interaction_count = interaction_count + 1";
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteArticle(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            long deletedCount = collection.deleteOne(new Document("_id", objectId)).getDeletedCount();
            return deletedCount > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static User getUserInfo(String email) {
        String sqlQuery = "SELECT * FROM user WHERE email = '" + email + "'";
        User user = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);

            if (rs.next()) {
                String name = rs.getString("username");
                String password = rs.getString("password");
                user = new User(email, name, password);
            } else {
                System.out.println("No user found with the given email.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user information: " + e.getMessage());
        }

        return user;
    }

    private static ObservableList<String> getUserHistory(String email) {
        String sqlQuery = "SELECT article_id, interaction_type, interaction_count FROM history WHERE email = '" + email + "'";
        ObservableList<String> historyList = FXCollections.observableArrayList();

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlQuery);

                while (rs.next()) {
                    String articleId = rs.getString("article_id");
                    String interactionType = rs.getString("interaction_type");
                    int interactionCount = rs.getInt("interaction_count");

                    String historyRecord = "Article ID: " + articleId +
                            ", Interaction: " + interactionType +
                            ", Count: " + interactionCount;
                    historyList.add(historyRecord);
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving user history: " + e.getMessage());
            }
        }
        return historyList;
    }

    public static CompletableFuture<ObservableList<String>> getUserHistoryAsync(String email) {
        return CompletableFuture.supplyAsync(() -> getUserHistory(email), executorService);
    }

    private static Map<String, Article> fetchAllArticles() {
        Map<String, Article> articlesMap = new HashMap<>();

        for (Document doc : collection.find()) {
            String title = doc.getString("title");
            String content = doc.getString("content");
            String author = doc.getString("author");
            String id = (doc.get("_id") instanceof ObjectId)
                    ? doc.getObjectId("_id").toString()
                    : doc.getString("_id");
            articlesMap.put(title, new Article(id, title, author, content));
        }

        return articlesMap;
    }

    // fetchAllArticles concurrent
    public static CompletableFuture<Map<String, Article>> fetchAllArticlesAsync() {
        return CompletableFuture.supplyAsync(DatabaseHandler::fetchAllArticles, executorService);
    }
}