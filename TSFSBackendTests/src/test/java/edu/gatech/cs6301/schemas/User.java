package edu.gatech.cs6301.schemas;

import com.google.gson.annotations.SerializedName;


import java.util.Objects;


public class User {

    @SerializedName("GT-username")
    String GT_username;

    String name;

    String email;
    public enum Affiliation {
        faculty,
        staff
    }

    Affiliation affiliation;

    public User(String GT_username, String name, String email, Affiliation affiliation) {
        this.GT_username = GT_username;
        this.name = name;
        this.email = email;
        this.affiliation = affiliation;
    }

    public String getGT_username() {
        return GT_username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Affiliation getAffiliation() {
        return affiliation;
    }

    public void setGT_username(String GT_username) {
        this.GT_username = GT_username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getGT_username(), user.getGT_username()) && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail()) && getAffiliation() == user.getAffiliation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGT_username(), getName(), getEmail(), getAffiliation());
    }

    @Override
    public String toString() {
        return "User{" +
                "GT_username='" + GT_username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", affiliation=" + affiliation +
                '}';
    }
}
