package com.example.travelgram.DAO;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PlaceDAO {
    private final FirebaseDatabase database;
    private static PlaceDAO instance;
    private MutableLiveData<String> createPlaceResponse;
    private MutableLiveData<HashMap<LatLng, String>> markerResponse;
    private MutableLiveData<HashMap<Place, Image>> placeInfoResponse;
    private MutableLiveData<String> createPostToPlaceImageResponse;

    private PlaceDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
        createPlaceResponse = new MutableLiveData<>();
        markerResponse = new MutableLiveData<>();
        placeInfoResponse = new MutableLiveData<>();
        createPostToPlaceImageResponse = new MutableLiveData<>();
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
            HashMap<LatLng, String> markersInArea = new HashMap<>();
            markersInArea.put(latLng, place.getPlaceName());
            markersInArea.putAll(markerResponse.getValue());
            markerResponse.setValue(markersInArea);
        } catch (Exception e) {
            Log.d("PlaceDAO", e.getMessage());
        }
    }

    public MutableLiveData<HashMap<LatLng, String>> getMarkerResponse() {
        return markerResponse;
    }

    public void getMarkersInArea(LatLngBounds bounds) {
        HashMap<LatLng, String> markersInArea = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("places");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    double latt = Double.parseDouble(ds.child("latitude").getValue(String.class));
                    double longg = Double.parseDouble(ds.child("longitude").getValue(String.class));
                    String placeName = ds.child("placeName").getValue(String.class);
                    LatLng latLng = new LatLng(latt, longg);
                    if(bounds.contains(latLng))
                        markersInArea.put(latLng, placeName);
                }
                markerResponse.setValue(markersInArea);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("PlaceDAO", databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<HashMap<Place, Image>> getPlaceInfoResponse() {
        return placeInfoResponse;
    }

    public void getPlaceInfo(LatLng position) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("places");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    double latt = Double.parseDouble(ds.child("latitude").getValue(String.class));
                    double longg = Double.parseDouble(ds.child("longitude").getValue(String.class));
                    LatLng latLng = new LatLng(latt, longg);

                    if(position.equals(latLng)) {
                        HashMap<Place, Image> place = new HashMap<>();
                        //List<Post> posts = new ArrayList<>();
                        Place value = new Place();

                       /* for (DataSnapshot d: ds.child("posts").getChildren()) {
                            Post post = new Post();
                            post.setPostID(d.child("postID").getValue(String.class));
                            post.setUserID(d.child("userID").getValue(String.class));
                            post.setLikeCount(0); //TODO do this bs
                            post.setDateOfCreation(d.child("dateOfCreation").getValue(String.class));
                            post.setContent(d.child("content").getValue(String.class));

                            if(d.child("comments").exists()); //TODO do this

                            posts.add(post);
                        }*/

                        value.setPlaceID(ds.getKey());
                        value.setDescription(ds.child("description").getValue(String.class));
                        value.setLatitude(ds.child("latitude").getValue(String.class));
                        value.setLongitude(ds.child("longitude").getValue(String.class));
                        value.setPlaceName(ds.child("placeName").getValue(String.class));
                        //value.setPosts(posts);

                        place.put(value, null);
                        placeInfoResponse.setValue(place);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("PlaceDAO", databaseError.getMessage());
            }
        });
    }

    public void setCreatePostToPlaceImageResponse(String response) {
        createPostToPlaceImageResponse.setValue(response);
    }

    public MutableLiveData<String> getCreatePostToPlaceImageResponse() {
        return createPostToPlaceImageResponse;
    }

    public void createPost(Place place, Post post) {
        try {
            DatabaseReference reference = database.getReference();
            reference.child("posts").child(place.getPlaceID()).child(post.getPostID()).setValue(post);
            setCreatePostToPlaceImageResponse("true");
        } catch (Exception e) {
            Log.d("PlaceDAO", e.getMessage());
        }
    }
}
