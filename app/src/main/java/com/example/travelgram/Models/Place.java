package com.example.travelgram.Models;

import java.util.List;

public class Place {
    private String placeName;
    private String description;
    private String latitude;
    private String longitude;
    private String placeID;
    private List<Post> posts;
    private String placeImageID;

    public Place() {
    }

    public Place(String placeName, String description, String latitude, String longitude, String placeID, List<Post> posts) {
        this.placeName = placeName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeID = placeID;
        this.posts = posts;
    }

    public Place(String placeName, String description, String latitude, String longitude, String placeID, List<Post> posts, String placeImageID) {
        this.placeName = placeName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeID = placeID;
        this.posts = posts;
        this.placeImageID = placeImageID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getPlaceImageID() {
        return placeImageID;
    }

    public void setPlaceImageID(String placeImageID) {
        this.placeImageID = placeImageID;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeName='" + placeName + '\'' +
                ", description='" + description + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", placeID='" + placeID + '\'' +
                ", posts=" + posts +
                '}';
    }
}
