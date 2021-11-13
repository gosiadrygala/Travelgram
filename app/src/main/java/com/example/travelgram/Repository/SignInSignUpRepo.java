package com.example.travelgram.Repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.DAO.SignInSignUpDAO;
import com.example.travelgram.Models.User;
import com.example.travelgram.Models.UserLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInSignUpRepo {
    private final UserLiveData currentUser;
    private final Application app;
    private static SignInSignUpRepo instance;
    private final FirebaseAuth mAuth;
    private MutableLiveData<String> response;
    private final SignInSignUpDAO signInSignUpDAO;

    private SignInSignUpRepo(Application app) {
        this.app = app;
        currentUser = new UserLiveData();
        mAuth = FirebaseAuth.getInstance();
        response = new MutableLiveData<>();
        signInSignUpDAO = SignInSignUpDAO.getInstance();
    }

    public static synchronized SignInSignUpRepo getInstance(Application app) {
        if(instance == null)
            instance = new SignInSignUpRepo(app);
        return instance;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getResponse() {
        return response;
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(app.getApplicationContext());
    }

    public void signUp(User user) {
        boolean userByUsername = signInSignUpDAO.getUserByUsername(user.getUsername());
        if(!userByUsername) {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).
                    addOnCompleteListener(app.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signOut();
                                String uid = mAuth.getUid();
                                signInSignUpDAO.createNewUser(uid, user);
                                response.postValue("Account created, please sign in.");
                            } else {
                                response.postValue(task.getException().getMessage());
                            }
                        }
                    });
        } else response.postValue("User with this username already exists.");
    }
}
