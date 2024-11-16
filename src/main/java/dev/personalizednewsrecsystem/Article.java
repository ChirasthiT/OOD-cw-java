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

    public void setId(String id) {
        this.id = id;
    }

}
