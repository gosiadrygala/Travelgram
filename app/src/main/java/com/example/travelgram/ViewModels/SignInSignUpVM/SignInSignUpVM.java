package com.example.travelgram.ViewModels.SignInSignUpVM;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.travelgram.Models.User;
import com.example.travelgram.Repository.SignInSignUpRepo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

public class SignInSignUpVM extends AndroidViewModel {

    private final SignInSignUpRepo signInSignUpRepo;

    public SignInSignUpVM(@NotNull Application app) {
        super(app);
        signInSignUpRepo = SignInSignUpRepo.getInstance(app);
    }

    public String signIn(String email, String password) {

        return "true";
    }

    public String signUp(String username, String email, String password, String shortDesc) {
        if(email.equals("") || username.equals("") || password.equals("") || shortDesc.equals(""))
            return "Please fill out all fields";
        if(username.length() < 5)
            return "Username is too short.";
        if(shortDesc.length() < 10)
            return "Short description is too short.";
        else {
            User user = new User(email, username, password, shortDesc, "");
            signInSignUpRepo.signUp(user);
            //signInSignUpRepo.signOut();
        }
        return "true";
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return signInSignUpRepo.getCurrentUser();
    }

    public LiveData<String> getResponse() {
        return signInSignUpRepo.getResponse();
    }

    public void signOut() {
        signInSignUpRepo.signOut();
    }
}
