package com.example.travelgram.Repository;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.DAO.PlaceDAO;
import com.example.travelgram.Models.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;

public class PlaceRepo {
    private static PlaceRepo instance;
    private final Application app;
    private final StorageReference mStorage;
    private final PlaceDAO placeDAO;

    public PlaceRepo(Application app) {
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
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                //setCreatePlaceResponse("Uploading the image successful.");
                place.setPlaceID(uuid.toString());
                placeDAO.createPlace(place);
            }
        });
    }
}
