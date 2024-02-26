package edu.gatech.cs6301.dto;

import java.util.ArrayList;
import java.util.Date;

public class Ticket {

    private int id;
    private String title;
    private String description;
    private Status status;
    private Category category;
    private String emailAddress;
    private String assignee;
    private ArrayList<String> watchers;
    private Date createdDate;
    private Date modifiedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public ArrayList<String> getWatchers() {
        return watchers;
    }

    public void setWatchers(ArrayList<String> watchers) {
        this.watchers = watchers;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public Ticket(){

    }
    public Ticket(String title, String description, Status status, Category category, String emailAddress, String assignee, ArrayList<String> watchers, Date createdDate, Date modifiedDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.category = category;
        this.emailAddress = emailAddress;
        this.assignee = assignee;
        this.watchers = watchers;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
