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
}
