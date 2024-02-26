package edu.gatech.cs6301.backend3.models;

import java.util.Objects;

public class Comment {

    private String author;
    private String createdDate;
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getContent() {
        return content;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(author, comment.author) && Objects.equals(createdDate, comment.createdDate) && Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, createdDate, content);
    }
}
