package com.example.travelgram.ViewModels.PlaceVM;

import android.app.Application;
import android.media.Image;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.DAO.PlaceDAO;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.example.travelgram.Repository.PlaceRepo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PlaceVM extends AndroidViewModel {

    private final PlaceRepo placeRepo;
    private MutableLiveData<String> createPlaceResponse;
    private final PlaceDAO placeDAO;

    public PlaceVM(@NotNull Application app) {
        super(app);
        placeRepo = PlaceRepo.getInstance(app);
        createPlaceResponse = new MutableLiveData<>();
        placeDAO = PlaceDAO.getInstance();
    }

    public void createPlace(Place place, Uri image) {
        if(place.getPlaceName().equals("") || place.getDescription().equals(""))
            setCreatePlaceResponse("Name of the place or description of the place is empty.");
        else if(image == null)
            setCreatePlaceResponse("Image of the place is empty.");
        else {
            placeRepo.createPlaceImage(place, image);
        }
    }

    public MutableLiveData<String> getCreatePlaceResponse() {
        return placeRepo.getCreatePlaceResponse();
    }

    public void setCreatePlaceResponse(String response) {
        placeRepo.setCreatePlaceResponse(response);
    }

    public void getMarkersInArea(LatLngBounds bounds) {
        placeDAO.getMarkersInArea(bounds);
    }

    public MutableLiveData<HashMap<LatLng, String>> getMarkerResponse() {
        return placeDAO.getMarkerResponse();
    }

    public void getPlaceInfo(LatLng position) {
        placeDAO.getPlaceInfo(position);
    }

    public MutableLiveData<HashMap<Place, Image>> getPlaceInfoResponse() {
        return placeDAO.getPlaceInfoResponse();
    }

    public void getPlacePicture(String placeID) {
        placeRepo.getPlacePicture(placeID);
    }

    public MutableLiveData<HashMap<String, byte[]>> getPlacePictureResponse() {
        return placeRepo.getGetPlacePictureResponse();
    }

    public void createPostToPlace(Place place, String postContent, Uri image) {
        if(place.getPlaceID().equals("") || place.getPlaceID().isEmpty()) {
            setCreatePostToPlaceImageResponse("Something went wrong!");
        } else if(postContent.length() < 5) {
            setCreatePostToPlaceImageResponse("Post content is too short");
        } else if(image != null) {
            Post post = new Post("", postContent, "", 0, "", "userID", null);
            placeRepo.createPostToPlaceImage(place, post, image);
        } else {
            String dateAndTime = Calendar.getInstance().getTime().toString();
            UUID uuid = UUID.nameUUIDFromBytes(dateAndTime.getBytes());
            Post post = new Post(uuid.toString(), postContent, "", 0, dateAndTime, "userID", null);
            placeDAO.createPost(place, post);
        }
    }

    private void setCreatePostToPlaceImageResponse(String response) {
        placeDAO.setCreatePostToPlaceImageResponse(response);
    }

    public MutableLiveData<String> getCreatePostToPlaceImageResponse() {
        return placeDAO.getCreatePostToPlaceImageResponse();
    }
}
