package com.example.travelgram.Models;

public class Comment {

    private String commentID;
    private String userPictureID;
    private String username;
    private String dateOfCreation;
    private String content;

    public Comment() {
    }

    public Comment(String commentID, String userPictureID, String username, String dateOfCreation, String content) {
        this.commentID = commentID;
        this.userPictureID = userPictureID;
        this.username = username;
        this.dateOfCreation = dateOfCreation;
        this.content = content;
    }

    public Comment(String userPictureID, String username, String dateOfCreation, String content) {
        this.userPictureID = userPictureID;
        this.username = username;
        this.dateOfCreation = dateOfCreation;
        this.content = content;
    }

    public String getUserPictureID() {
        return userPictureID;
    }

    public void setUserPictureID(String userPictureID) {
        this.userPictureID = userPictureID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID='" + commentID + '\'' +
                ", userPictureID='" + userPictureID + '\'' +
                ", username='" + username + '\'' +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
