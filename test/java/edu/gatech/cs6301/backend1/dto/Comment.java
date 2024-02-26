package edu.gatech.cs6301.dto;

import java.util.Date;

public class Comment {
    private String author;

    private Date createdDate;

    private String content;

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

    public Comment(){

    }
    public Comment(String author, Date createdDate, String content) {
        this.author = author;
        this.createdDate = createdDate;
        this.content = content;
    }
}
