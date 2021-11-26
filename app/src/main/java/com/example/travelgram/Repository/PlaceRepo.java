package com.example.travelgram.Repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.travelgram.DAO.PlaceDAO;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PlaceRepo {
    private static PlaceRepo instance;
    private final Application app;
    private final StorageReference mStorage;
    private final PlaceDAO placeDAO;
    private final MutableLiveData<HashMap<String, byte[]>> getPlacePictureResponse;
    private final MutableLiveData<ArrayList<Post>> postsForPlaceWithPictureResponse;

    private final MutableLiveData<HashMap<Integer, Post>> postPictureResponse;

    private PlaceRepo(Application app) {
        this.app = app;
        mStorage = FirebaseStorage.getInstance().getReference();
        placeDAO = PlaceDAO.getInstance();
        getPlacePictureResponse = new MutableLiveData<>();
        postsForPlaceWithPictureResponse = new MutableLiveData<>();
        postPictureResponse = new MutableLiveData<>();
    }

    public static synchronized PlaceRepo getInstance(Application app) {
        if (instance == null)
            instance = new PlaceRepo(app);
        return instance;
    }

    public MutableLiveData<String> getCreatePlaceResponse() {
        return placeDAO.getCreatePlaceResponse();
    }

    public void setCreatePlaceResponse(String response) {
        placeDAO.setCreatePlaceResponse(response);
    }

    private void setCreatePostToPlaceImageResponse(String response) {
        placeDAO.setCreatePostToPlaceImageResponse(response);
    }

    public void createPlaceImage(Place place, Uri image) {
        UUID uuid = UUID.nameUUIDFromBytes(Calendar.getInstance().getTime().toString().getBytes());
        StorageReference riversRef = mStorage.child("placeImages/" + uuid.toString());
        UploadTask uploadTask = riversRef.putFile(image);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                setCreatePlaceResponse("Uploading the image failed. Please try again.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                place.setPlaceID(uuid.toString());
                placeDAO.createPlace(place);
            }
        });
    }

    public MutableLiveData<HashMap<String, byte[]>> getGetPlacePictureResponse() {
        return getPlacePictureResponse;
    }

    public void getPlacePicture(String placeID) {
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference("placeImages/" + placeID);
        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        HashMap<String, byte[]> response = new HashMap<>();
                        response.put(placeID, bytes);
                        getPlacePictureResponse.setValue(response);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("PlaceRepo", exception.getMessage());
            }
        });
    }

    public void createPostToPlaceImage(Place place, Post post, Uri image) {
        String dateAndTime = Calendar.getInstance().getTime().toString();
        UUID uuid = UUID.nameUUIDFromBytes(dateAndTime.getBytes());

        StorageReference riversRef = mStorage.child("postImages/" + uuid.toString());
        UploadTask uploadTask = riversRef.putFile(image);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                setCreatePostToPlaceImageResponse("Uploading the image failed. Please try again.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                post.setImageID(uuid.toString());
                post.setPostID(uuid.toString());
                post.setDateOfCreation(dateAndTime);
                placeDAO.createPost(place, post);
            }
        });
    }


    public MutableLiveData<ArrayList<Post>> getPostsForPlaceWithPictureResponse() {
        return postsForPlaceWithPictureResponse;
    }

    public void getImageForPost(Post post, int index) {
        StorageReference mImageRef =
                mStorage.getStorage().getReference("postImages/" + post.getPostID());
        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        post.setPicture(bytes);
                        HashMap<Integer, Post> posts = postPictureResponse.getValue();
                        if(posts == null){
                            posts = new HashMap<>();
                            posts.put(index, post);
                            postPictureResponse.setValue(posts);
                        }else {
                            posts.put(index, post);
                            postPictureResponse.setValue(posts);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println(exception.getMessage());
                Log.d("PlaceRepo", exception.getMessage());
            }
        });
    }

    public MutableLiveData<HashMap<Integer, Post>> getPostPictureResponse() {
        return postPictureResponse;
    }
}
