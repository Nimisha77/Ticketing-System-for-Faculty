package edu.gatech.cs6301.schemas;


import java.util.Date;
import java.util.Objects;


public class Comment {

    String author;
    Date createdDate;
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment(String author, Date createdDate, String content) {
        this.author = author;
        this.createdDate = createdDate;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(getAuthor(), comment.getAuthor()) && Objects.equals(getCreatedDate(), comment.getCreatedDate()) && Objects.equals(getContent(), comment.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthor(), getCreatedDate(), getContent());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author='" + author + '\'' +
                ", createdDate=" + createdDate +
                ", content='" + content + '\'' +
                '}';
    }
}
