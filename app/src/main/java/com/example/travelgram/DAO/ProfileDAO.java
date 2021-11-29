package com.example.travelgram.DAO;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileDAO {
    private final FirebaseDatabase database;
    private static ProfileDAO instance;
    private MutableLiveData<User> getUserInfoResponse;
    private MutableLiveData<String> followResponse;
    private MutableLiveData<Long> followCountResponse;

    private ProfileDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
        getUserInfoResponse = new MutableLiveData<>();
        followResponse = new MutableLiveData<>();
        followCountResponse = new MutableLiveData<>();
    }

    public static synchronized ProfileDAO getInstance() {
        if (instance == null)
            instance = new ProfileDAO();
        return instance;
    }

    public void getUserInfo(String email) {
        DatabaseReference reference = database.getReference();
        Query query = reference
                .child("users")
                .orderByChild("email")
                .equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsc : dataSnapshot.getChildren()) {
                    User user = dsc.getValue(User.class);
                    getUserInfoResponse.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ProfileDAO", databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<User> getUserInfoResponse() {
        return getUserInfoResponse;
    }

    public void getUserInfoByUsername(String username) {
        DatabaseReference reference = database.getReference();
        Query query = reference
                .child("users")
                .orderByChild("username")
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsc : dataSnapshot.getChildren()) {
                    User user = dsc.getValue(User.class);
                    getUserInfoResponse.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ProfileDAO", databaseError.getMessage());
            }
        });
    }

    public void followUnfollowProfile(String usernameToFollow, String email, String followBtnState) {
        email = email.replace(".", ",");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (!followBtnState.equalsIgnoreCase("follow")) {
            Query query = reference.child("followsUsers").child(usernameToFollow).child(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue();
                    followResponse.setValue("Follow");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("ProfileDAO", error.getMessage());
                }
            });
        } else {
            reference.child("followsUsers").child(usernameToFollow).child(email).setValue(email);
            followResponse.setValue("Unfollow");
        }
    }

    public MutableLiveData<String> getFollowResponse() {
        return followResponse;
    }

    public void getFollowState(String username, String email) {
        email = email.replace(".", ",");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("followsUsers")
                .child(username).child(email);
        String finalEmail = email;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null && snapshot.getValue().toString().equals(finalEmail))
                    followResponse.setValue("Unfollow");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileDAO", error.getMessage());
            }
        });
    }

    public void getFollowCount(String username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("followsUsers").child(username);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followCountResponse.setValue(snapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileDAO", error.getMessage());
            }
        });
    }

    public MutableLiveData<Long> getFollowCountResponse() {
        return followCountResponse;
    }
}
