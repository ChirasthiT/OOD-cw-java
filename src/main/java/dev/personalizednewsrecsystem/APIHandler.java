package dev.personalizednewsrecsystem;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class APIHandler {
    private static String recommend = "http://127.0.0.1:8000/recommend";
    private static String addOrUpdate = "http://127.0.0.1:8000/add_or_update_article";

    public static Queue<Article> getRecommendations(String preferences) {
        Queue<Article> articles = new LinkedList<>();
        try {
            // Setup connection
            URL url = new URL(recommend);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON payload
            String jsonInput = "{\"preferences\": \"" + preferences + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
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

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recommendations", e);
        }
        return articles;
    }

    // Overloaded method to get only one article
//    public static Article getRecommendations(String preferences, boolean threeorone) {
//        List<Article> allArticles = getRecommendations(preferences);
//        return allArticles.getFirst();
//    }

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

