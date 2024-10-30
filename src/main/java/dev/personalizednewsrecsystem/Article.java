package dev.personalizednewsrecsystem;

public class Article {
    private String id, title, author, content;

    public Article(String id, String title, String author, String content) {
        this.author = author;
        this.title = title;
        this.id = id;
        this.content = content;
    }

    public Article(String title, String author, String content) {
        this.author = author;
        this.title = title;
        this.id = null;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String sendArticle() {
        return createJsonInput();
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    private String createJsonInput() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"title\": \"").append(escapeJson(title)).append("\",");
        jsonBuilder.append("\"author\": \"").append(escapeJson(author)).append("\",");
        jsonBuilder.append("\"content\": \"").append(escapeJson(content)).append("\"");

        if (id != null && !id.isEmpty()) {
            jsonBuilder.append(",\"id\": \"").append(id).append("\"");
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
