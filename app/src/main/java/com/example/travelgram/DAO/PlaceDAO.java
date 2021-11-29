package com.example.travelgram.DAO;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.Models.Comment;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.example.travelgram.Models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlaceDAO {
    private final FirebaseDatabase database;
    private static PlaceDAO instance;
    private MutableLiveData<String> createPlaceResponse;
    private MutableLiveData<HashMap<LatLng, String>> markerResponse;
    private MutableLiveData<Place> placeInfoResponse;
    private MutableLiveData<String> createPostToPlaceImageResponse;
    private MutableLiveData<ArrayList<Post>> postsForPlaceResponse;
    private MutableLiveData<Boolean> followResponse;
    private MutableLiveData<String> createCommentResponse;
    private MutableLiveData<String> usernameResponse;

    private PlaceDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
        createPlaceResponse = new MutableLiveData<>();
        markerResponse = new MutableLiveData<>();
        placeInfoResponse = new MutableLiveData<>();
        createPostToPlaceImageResponse = new MutableLiveData<>();
        postsForPlaceResponse = new MutableLiveData<>();
        followResponse = new MutableLiveData<>();
        createCommentResponse = new MutableLiveData<>();
        usernameResponse = new MutableLiveData<>();
    }

    public static synchronized PlaceDAO getInstance() {
        if (instance == null)
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
            markersInArea.putAll(Objects.requireNonNull(markerResponse.getValue()));
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
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    double latt = Double.parseDouble(ds.child("latitude").getValue(String.class));
                    double longg = Double.parseDouble(ds.child("longitude").getValue(String.class));
                    String placeName = ds.child("placeName").getValue(String.class);
                    LatLng latLng = new LatLng(latt, longg);
                    if (bounds.contains(latLng))
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

    public MutableLiveData<Place> getPlaceInfoResponse() {
        return placeInfoResponse;
    }

    public void getPlaceInfo(LatLng position) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("places");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    double latt = Double.parseDouble(ds.child("latitude").getValue(String.class));
                    double longg = Double.parseDouble(ds.child("longitude").getValue(String.class));
                    LatLng latLng = new LatLng(latt, longg);

                    if (position.equals(latLng)) {
                        Place value = new Place();
                        value = ds.getValue(Place.class);
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

                        //value.setPlaceID(ds.getKey());
                        //value.setDescription(ds.child("description").getValue(String.class));
                        //value.setLatitude(ds.child("latitude").getValue(String.class));
                        //value.setLongitude(ds.child("longitude").getValue(String.class));
                        //value.setPlaceName(ds.child("placeName").getValue(String.class));
                        //value.setPosts(posts);

                        placeInfoResponse.setValue(value);
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

            Query query = reference
                    .child("users")
                    .orderByChild("email")
                    .equalTo(post.getUserEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = new User();
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        user = ds.getValue(User.class);
                    }
                    post.setUsername(user.getUsername());
                    post.setUserPicture(user.getPictureID());

                    reference.child("posts").child(place.getPlaceID()).child(post.getPostID()).setValue(post);
                    setCreatePostToPlaceImageResponse("true");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("PlaceDAO", databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("PlaceDAO", e.getMessage());
        }
    }


    public void followUnfollowPlace(String placeID, String email, boolean followBtnState) {
        email = email.replace(".", ",");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (followBtnState) {
            Query query = reference.child("follows").child(placeID).child(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue();
                    followResponse.setValue(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("PlaceDAO", error.getMessage());
                }
            });
        } else {
            reference.child("follows").child(placeID).child(email).setValue(email);
            followResponse.setValue(true);
        }
    }

    public MutableLiveData<Boolean> getFollowResponse() {
        return followResponse;
    }

    public void getFollowState(String placeID, String email) {
        email = email.replace(".", ",");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("follows").child(placeID).child(email);
        String finalEmail = email;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null && snapshot.getValue().toString().equals(finalEmail)) {
                    followResponse.setValue(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PlaceDAO", error.getMessage());
            }
        });
    }

    public void deletePost(String postId, String placeID) {
        try{
        database.getReference().child("posts").child(placeID).child(postId).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeUnlikeThisPost(String postID, String email, String placeID) {
        DatabaseReference reference = database.getReference();
            Query query = reference.child("posts").child(placeID).child(postID).child("likedByUsers");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                    List<String> value = snapshot.getValue(t);
                    if(value == null) {
                        value = new ArrayList<>();
                        value.add(email);
                        reference.child("posts").child(placeID).child(postID).child("likedByUsers").setValue(value);
                    } else {
                        for (String val: value) {
                            if(val.equals(email)) {
                                value.remove(val);
                                break;
                            }
                        }
                        reference.child("posts").child(placeID).child(postID).child("likedByUsers").setValue(value);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("PlaceDAO", error.getMessage());
                }
            });
    }

    public MutableLiveData<String> getCreateCommentResponse() {
        return createCommentResponse;
    }

    public void createComment(String postID, String userEmail, String commentContent) {
        DatabaseReference reference = database.getReference();
        Query query = reference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();

                for (DataSnapshot dsc: snapshot.getChildren()) {
                    user = dsc.getValue(User.class);
                    if(user.getEmail().equals(userEmail))
                        break;
                }

                Date dateAndTime = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateOfCreation = simpleDateFormat.format(dateAndTime);

                UUID uuid = UUID.nameUUIDFromBytes(dateAndTime.toString().getBytes());

                Comment comment = new Comment(user.getPictureID(), user.getUsername(),
                        dateOfCreation, commentContent);
                comment.setCommentID(uuid.toString());

                try {
                    reference.child("comments").child(postID).child(uuid.toString()).setValue(comment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PlaceDAO", error.getMessage());
            }
        });
    }

    public void getUsernameByEmail(String userEmail) {
        DatabaseReference reference = database.getReference();
        Query query = reference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();

                for (DataSnapshot dsc: snapshot.getChildren()) {
                    user = dsc.getValue(User.class);
                    if(user.getEmail().equals(userEmail))
                        break;
                }

                usernameResponse.setValue(user.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PlaceDAO", error.getMessage());
            }
        });

    }

    public MutableLiveData<String> getUsernameResponse() {
        return usernameResponse;
    }

    public void deleteComment(String postID, String commentID) {
        try{
            database.getReference().child("comments").child(postID).child(commentID).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
