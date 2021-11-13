package com.example.travelgram.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travelgram.Models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInSignUpDAO {
    private final FirebaseDatabase database;
    private static SignInSignUpDAO instance;

    private SignInSignUpDAO() {
        database = FirebaseDatabase.getInstance("https://travelgram-67699-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public static synchronized SignInSignUpDAO getInstance() {
        if(instance == null)
            instance = new SignInSignUpDAO();
        return instance;
    }

    public boolean getUserByUsername(String username) {
        final boolean[] exists = {false, false};
        /*Query query1 = database.getReference().child("users").orderByChild("username");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });*/
        /*do {
            Query query = database.getReference().child("users").orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0) {
                        exists[0] = true;
                        exists[1] = true;
                    } else exists[1] = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        } while (!exists[1]);
        return exists[0];*/

        /*final DatabaseReference dinosaursRef = database.getReference("users");
        dinosaursRef.orderByChild("username").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User dinosaur = dataSnapshot.getValue(User.class);
                System.out.println(dataSnapshot.getKey() + " was " + dinosaur + " meters tall.");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        Query query = database.getReference().child("users").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String message;
                    do{
                        message = dataSnapshot.getValue().toString();
                        if(message.equals(null))
                            break;
                        else exists[0] = true;
                    }while
                    (!message.equals(null));
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
        return exists[0];
    }

    public void createNewUser(String uid, User user) {
        try {
            database.getReference().child("users").child(uid).setValue(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
