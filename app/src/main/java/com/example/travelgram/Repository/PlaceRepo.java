package com.example.travelgram.Repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.travelgram.DAO.PlaceDAO;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/* Class used for accessing the Firebase storage */
public class PlaceRepo {
    private static PlaceRepo instance;
    private final Application app;
    private final StorageReference mStorage;
    private final PlaceDAO placeDAO;

    private PlaceRepo(Application app) {
        this.app = app;
        mStorage = FirebaseStorage.getInstance().getReference();
        placeDAO = PlaceDAO.getInstance();
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

    /* Method used for saving the place image to the firebase storage and creating the place */
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
                createImageAndPlace(place, image);
            }
        });
    }

    /* Method used for retrieving the url of the place image and for
    * creating the place using PlaceDAO */
    private void createImageAndPlace(Place place, Uri image) {
        final StorageReference ref = mStorage.child("placeImages/" + place.getPlaceID());
        UploadTask uploadTask = ref.putFile(image);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    place.setPlaceImageID(downloadUri.toString());
                    placeDAO.createPlace(place);
                } else {
                    setCreatePlaceResponse(task.getException().getMessage());
                }
            }
        });
    }

    /* Method used for saving the post image to the firebase storage, getting the url of the image
    and then saving the data about the post to the database using PlaceDAO*/
    @SuppressLint("SimpleDateFormat")
    public void createPostToPlaceImage(Place place, Post post, Uri image) {
        Date dateAndTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(dateAndTime);
        UUID uuid = UUID.nameUUIDFromBytes(dateAndTime.toString().getBytes());

        final StorageReference ref = mStorage.child("postImages/" + uuid.toString());
        UploadTask uploadTask = ref.putFile(image);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    post.setPostPicture(downloadUri.toString());
                    post.setPostID(uuid.toString());

                    post.setDateOfCreation(date);
                    placeDAO.createPost(place, post);
                } else {
                    setCreatePostToPlaceImageResponse("Uploading the image failed. Please try again.");
                }
            }
        });
    }

}
