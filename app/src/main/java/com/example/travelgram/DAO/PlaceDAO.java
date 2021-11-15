package com.example.travelgram.DAO;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.Models.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceDAO {
    private final FirebaseDatabase database;
    private static PlaceDAO instance;
    private MutableLiveData<String> createPlaceResponse;
    private MutableLiveData<HashMap<String, LatLng>> markerResponse;

    private PlaceDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
        createPlaceResponse = new MutableLiveData<>();
        markerResponse = new MutableLiveData<>();
    }

    public static synchronized PlaceDAO getInstance() {
        if(instance == null)
            instance = new PlaceDAO();
        return instance;
    }

    public MutableLiveData<String> getCreatePlaceResponse() {
        return createPlaceResponse;
    }

    public void setCreatePlaceResponse(String response) {
        createPlaceResponse.setValue(response);
    }

    public void createPlace(Place place) {
        try {
            database.getReference().child("places").child(place.getPlaceID()).setValue(place); //TODO remove placeID fromm the place object
            LatLng latLng = new LatLng(Double.parseDouble(place.getLatitude()), Double.parseDouble(place.getLongitude()));
            database.getReference().child("markers").child(place.getPlaceID()).setValue(latLng);
            createPlaceResponse.setValue("Place created.");
            HashMap<String, LatLng> markersInArea = new HashMap<>();
            markersInArea.put(place.getPlaceID(), latLng);
            markersInArea.putAll(markerResponse.getValue());
            markerResponse.setValue(markersInArea);
        } catch (Exception e) {
            Log.d("PlaceDAO", e.getMessage());
        }
    }

    public MutableLiveData<HashMap<String, LatLng>> getMarkerResponse() {
        return markerResponse;
    }

    public void getMarkersInArea(LatLngBounds bounds) {
        HashMap<String, LatLng> markersInArea = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("markers");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    double latt = (double) ds.child("latitude").getValue();
                    double longg = (double) ds.child("longitude").getValue();
                    LatLng latLng = new LatLng(latt, longg);
                    if(bounds.contains(latLng))
                        markersInArea.put(ds.getKey(), latLng);
                }
                markerResponse.setValue(markersInArea);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("PlaceDAO", databaseError.getMessage());
            }
        });
    }
}
