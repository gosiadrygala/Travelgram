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

public class SignInSignUpDAO {
    private final FirebaseDatabase database;
    private static SignInSignUpDAO instance;
    private final MutableLiveData<String> usernameExists;

    private SignInSignUpDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
        usernameExists = new MutableLiveData<>();
    }

    public static synchronized SignInSignUpDAO getInstance() {
        if(instance == null)
            instance = new SignInSignUpDAO();
        return instance;
    }

    /* Method used to check if the specified register username exists in the database */
    public void getUserByUsername(String username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("users")
                .orderByChild("username")
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                   usernameExists.setValue("username found");

                }else{
                    usernameExists.setValue("username not found");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SignInSignUpDAO", databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<String> getUsernameExists() {
        return usernameExists;
    }

    /* Method used to created a new user in the database */
    public void createNewUser(String uid, User user) {
        try {
            user.setPassword("null");
            database.getReference().child("users").child(uid).setValue(user);
        } catch (Exception e) {
            Log.d("SignInSignUpDAO", e.getMessage());
        }
    }
}
