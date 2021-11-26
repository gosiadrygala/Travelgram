package com.example.travelgram.Models;


import java.util.List;

public class Post {
    private String postID;
    private String content;
    private String imageID;
    private int likeCount;
    private String dateOfCreation;
    private String userID;
    private List<Comment> comments;

    public Post() {
    }

    public Post(String postID, String content, String imageID, int likeCount, String dateOfCreation, String userID, List<Comment> comments) {
        this.postID = postID;
        this.content = content;
        this.imageID = imageID;
        this.likeCount = likeCount;
        this.dateOfCreation = dateOfCreation;
        this.userID = userID;
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

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postID='" + postID + '\'' +
                ", content='" + content + '\'' +
                ", imageID='" + imageID + '\'' +
                ", likeCount=" + likeCount +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", userID='" + userID + '\'' +
                ", comments=" + comments +
                '}';
    }
}
