package dev.personalizednewsrecsystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
public class APIHandler {
    private static String recommend = "http://127.0.0.1:8000/recommend";
    private static String addOrUpdate = "http://127.0.0.1:8000/add_or_update_article";

    public static Article getRecommendations(String preferences) {
        try {
            // Open a connection
            URL url = new URL(recommend);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON payload
            String jsonInput = "{\"preferences\": \"" + preferences + "\"}";

            // Request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Response to JSON
            JSONArray articlesArray = new JSONArray(response.toString());

            // Assuming the response contains an array of articles, we'll return the first one
            if (!articlesArray.isEmpty()) {
                JSONObject firstArticle = articlesArray.getJSONObject(0);
                String id = firstArticle.getString("id");
                String title = firstArticle.getString("title");
                String author = firstArticle.getString("author");
                String content = firstArticle.getString("content");

                return new Article(id, title, author, content);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Article> getRecommendations(String preferences, boolean threeorone) {
        List<Article> articles = new ArrayList<>();

        try {
            URL url = new URL(recommend + "?preferences=" + preferences);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(response.toString());

            for (int i = 0; i < jsonArray.length() && i < 3; i++) {
                JSONObject jsonArticle = jsonArray.getJSONObject(i);

                Article article = new Article(
                        jsonArticle.getString("id"),
                        jsonArticle.getString("title"),
                        jsonArticle.getString("author"),
                        jsonArticle.getString("content")
                );

                articles.add(article);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return articles;
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

