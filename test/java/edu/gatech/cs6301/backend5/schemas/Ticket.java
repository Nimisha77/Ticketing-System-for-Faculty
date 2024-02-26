package edu.gatech.cs6301.schemas;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Date;


public class Ticket {

    Integer id;
    String title;
    String description;

    public enum Status {
        open,
        closed,
        needs_attention,
        error
    }

    Status status;

    public enum Category {
        travel_authorization,
        reimbursement,
        meeting_organization,
        student_hiring,
        proposals,
        miscellaneous,
        error
    }

    Category category;

    String emailAddress;

    String assignee;

    ArrayList<String> watchers;
    Date createdDate;
    Date modifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        return Objects.equals(getId(), ticket.getId()) && Objects.equals(getTitle(), ticket.getTitle()) && Objects.equals(getDescription(), ticket.getDescription()) && getStatus() == ticket.getStatus() && getCategory() == ticket.getCategory() && Objects.equals(getEmailAddress(), ticket.getEmailAddress()) && Objects.equals(getAssignee(), ticket.getAssignee()) && Objects.equals(getWatchers(), ticket.getWatchers()) && Objects.equals(getCreatedDate(), ticket.getCreatedDate()) && Objects.equals(getModifiedDate(), ticket.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getStatus(), getCategory(), getEmailAddress(), getAssignee(), getWatchers(), getCreatedDate(), getModifiedDate());
    }

    public Ticket(Integer id, String title, String description, Status status, Category category, String emailAddress, String assignee, ArrayList<String> watchers, Date createdDate, Date modifiedDate) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", category=" + category +
                ", emailAddress='" + emailAddress + '\'' +
                ", assignee='" + assignee + '\'' +
                ", watchers=" + watchers +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}


