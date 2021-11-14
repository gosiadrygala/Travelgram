package com.example.travelgram.Repository;

import android.annotation.SuppressLint;
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
    private final MutableLiveData<String> signUpResponse;
    private final MutableLiveData<String> signInResponse;
    private final SignInSignUpDAO signInSignUpDAO;

    private SignInSignUpRepo(Application app) {
        this.app = app;
        currentUser = new UserLiveData();
        mAuth = FirebaseAuth.getInstance();
        signUpResponse = new MutableLiveData<>();
        signInSignUpDAO = SignInSignUpDAO.getInstance();
        signInResponse = new MutableLiveData<>();
    }

    public static synchronized SignInSignUpRepo getInstance(Application app) {
        if (instance == null)
            instance = new SignInSignUpRepo(app);
        return instance;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getSignUpResponse() {
        return signUpResponse;
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(app.getApplicationContext());
    }

    /* Method used to check if the specified register username exists */
    public void getUserByUsername(String username) {
        signInSignUpDAO.getUserByUsername(username);
    }

    public MutableLiveData<String> getUsernameExists() {
        return signInSignUpDAO.getUsernameExists();
    }

    /* Method used for registering the user in the firebase authentication service, as well as
    adding the data about the user to database */
    @SuppressLint("NewApi")
    public void signUp(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).
                addOnCompleteListener(app.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signOut();
                            String uid = mAuth.getUid();
                            signInSignUpDAO.createNewUser(uid, user);
                            signUpResponse.postValue("Account created, please sign in.");
                        } else {
                            signUpResponse.postValue(task.getException().getMessage());
                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(app.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {
                            signInResponse.postValue(task.getException().getMessage());
                        }
                    }
                });
    }

    public MutableLiveData<String> getSignInResponse() {
        return signInResponse;
    }
}
