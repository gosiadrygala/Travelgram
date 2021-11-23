package com.example.travelgram.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String email;
    private String username;
    private String password;
    private String shortDescription;
    private String pictureID;

    public User() {
    }

    public User(String email, String username, String password, String shortDescription, String pictureID) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.shortDescription = shortDescription;
        this.pictureID = pictureID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", picture='" + pictureID + '\'' +
                '}';
    }
}
