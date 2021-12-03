package com.example.travelgram.DAO;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.travelgram.Models.Comment;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.example.travelgram.Models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

/* A class used for reading, writing, and deleting the data
from the Realtime database regarding place, comment, post and user object */

public class PlaceDAO {
    private final FirebaseDatabase database;
    private static PlaceDAO instance;
    private MutableLiveData<String> createPlaceResponse;
    private MutableLiveData<HashMap<LatLng, String>> markerResponse;
    private MutableLiveData<Place> placeInfoResponse;
    private MutableLiveData<String> createPostToPlaceImageResponse;
    private MutableLiveData<Boolean> followResponse;
    private MutableLiveData<String> createCommentResponse;
    private MutableLiveData<String> usernameResponse;

    private PlaceDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
        createPlaceResponse = new MutableLiveData<>();
        markerResponse = new MutableLiveData<>();
        placeInfoResponse = new MutableLiveData<>();
        createPostToPlaceImageResponse = new MutableLiveData<>();
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

    /* Method used for creating the place object in the database */
    public void createPlace(Place place) {
        try {
            database.getReference().child("places").child(place.getPlaceID()).setValue(place);
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


    /* Method used for getting all the places within the specified bounds on the map - sets the
    markerResponse with all the found markers */
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

    /* Method used for getting all the data regarding the place with the specified geographical location */
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

    /* Method used for creating the post on the specified place channel */
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
                    String newEmail = user.getEmail().replace(".", ",");
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

    /* Method used for performing the follow operation,
    * if the user already follows the place the email of the user is deleted from the followers of this place,
    * if user does not follow the email is added to the followers of this place */
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


    /* Method used for checking whether the logged in user follows
    the place he/ she is currently visiting*/
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

    public MutableLiveData<Boolean> getFollowResponse() {
        return followResponse;
    }

    /* Method used for deleting the post object from the database */
    public void deletePost(String postId, String placeID) {
        try{
        database.getReference().child("posts").child(placeID).child(postId).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Method used for performing the like operation on the post,
    * if the user already likes the place the email is removed from the likedByUsers list,
    * if not the email is added */
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
                        boolean found = false;
                        String val1 = null;
                        for (String val: value) {
                            if(val.equals(email)) {
                                val1 = val;
                                found = true;
                                break;
                            }
                        }
                        if(found){
                            value.remove(val1);
                        }
                        else{
                            value.add(email);
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

    /* Method used for creating a comment to the post */
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

    public MutableLiveData<String> getCreateCommentResponse() {
        return createCommentResponse;
    }

    /* Method used for deleting a comment from the post */
    public void deleteComment(String postID, String commentID) {
        try{
            database.getReference().child("comments").child(postID).child(commentID).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Method used for getting the username of the user by the email specified */
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
}
