package com.example.travelgram.Models;


import java.util.List;

public class Post {
    private String postID;
    private String content;
    private String postPicture;
    private String userPicture;
    private int likeCount;
    private String dateOfCreation;
    private String username;
    private String userEmail;
    private List<Comment> comments;

    public Post() {
    }

    public Post(String postID, String content, String postPicture, String userPicture, int likeCount,
                String dateOfCreation,
                String username, String userEmail, List<Comment> comments) {
        this.postID = postID;
        this.content = content;
        this.postPicture = postPicture;
        this.userPicture = userPicture;
        this.likeCount = likeCount;
        this.dateOfCreation = dateOfCreation;
        this.username = username;
        this.userEmail = userEmail;
        this.comments = comments;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(String postPicture) {
        this.postPicture = postPicture;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postID='" + postID + '\'' +
                ", content='" + content + '\'' +
                ", postPicture='" + postPicture + '\'' +
                ", userPicture='" + userPicture + '\'' +
                ", likeCount=" + likeCount +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", comments=" + comments +
                '}';
    }
}
