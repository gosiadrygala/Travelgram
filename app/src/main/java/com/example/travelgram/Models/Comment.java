package com.example.travelgram.Models;

public class Comment {
    private String userID;
    private String nameOfTheAuthor;
    private String dateOfCreation;
    private String content;

    public Comment(String userID, String nameOfTheAuthor, String dateOfCreation, String content) {
        this.userID = userID;
        this.nameOfTheAuthor = nameOfTheAuthor;
        this.dateOfCreation = dateOfCreation;
        this.content = content;
    }

    public Comment() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNameOfTheAuthor() {
        return nameOfTheAuthor;
    }

    public void setNameOfTheAuthor(String nameOfTheAuthor) {
        this.nameOfTheAuthor = nameOfTheAuthor;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userID='" + userID + '\'' +
                ", nameOfTheAuthor='" + nameOfTheAuthor + '\'' +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
