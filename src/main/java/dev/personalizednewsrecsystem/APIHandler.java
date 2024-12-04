package dev.personalizednewsrecsystem;

import dev.personalizednewsrecsystem.Article;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class APIHandler {
    private static String recommend = "http://127.0.0.1:8001/recommend";
    private static String addOrUpdate = "http://127.0.0.1:8001/add_or_update_article";
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private static Queue<Article> getRecommendations(String preferences, String email) {
        Queue<Article> articles = new LinkedList<>();
        try {
            // Setup connection
            URL url = new URL(recommend);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON payload
            String jsonInput = "{\"email\": \"" + email + "\", \"preferences\": \"" + preferences + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error has occurred");
                alert.setContentText("No Articles found for your preferences");
                return articles;  // Return an empty queue if 404
            }

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray jsonArray = jsonResponse.getJSONArray("recommended_articles");

            // Limit to 3 articles
            for (int i = 0; i < jsonArray.length() && i < 3; i++) {
                JSONObject jsonArticle = jsonArray.getJSONObject(i);
                Article article = new Article(
                        jsonArticle.optString("id", "N/A"),
                        jsonArticle.optString("title", "Untitled"),
                        jsonArticle.optString("author", "Unknown"),
                        jsonArticle.optString("content", "No content available")
                );
                articles.add(article);
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred");
            alert.setContentText("Error connecting the API endpoint");
            alert.showAndWait();
        }
        return articles;
    }

    // getRecommendations concurrent
    public static CompletableFuture<Queue<Article>> getRecommendationsAsync(String preferences, String email) {
        return CompletableFuture.supplyAsync(() -> getRecommendations(preferences, email), executorService);
    }

    public static void addorUpdate(Article article) {
        try {
            URL url = new URL(addOrUpdate);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonArticle = new JSONObject();
            jsonArticle.put("id", article.getId());
            jsonArticle.put("title", article.getTitle());
            jsonArticle.put("author", article.getAuthor());
            jsonArticle.put("content", article.getContent());

            String jsonInputString = jsonArticle.toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Article added/updated successfully!");
            } else {
                System.out.println("Failed to add/update the article. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred");
            alert.setContentText("Error connecting the API endpoint");
            alert.showAndWait();
        }
    }
}

