package com.example.travelgram.ViewModels.SignInSignUpVM;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    public void signIn(String email, String password) {
        signInSignUpRepo.signIn(email, password);
    }

    public LiveData<String> getSignInResponse() {
        return signInSignUpRepo.getSignInResponse();
    }

    /* Method used for validating the input from user, including check
     * for the existing username
     */
    public String validateFields(String username, String email, String password, String shortDesc, Uri image) {
        if(email.equals("") || username.equals("") || password.equals("") || shortDesc.equals(""))
            return "Please fill out all fields";
        if(username.length() < 5)
            return "Username is too short.";
        if(shortDesc.length() < 10)
            return "Short description is too short.";
        if(image == null)
            return "Upload a profile picture first.";

        signInSignUpRepo.getUserByUsername(username);
        return "true";
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return signInSignUpRepo.getCurrentUser();
    }

    public LiveData<String> getSignUpResponse() {
        return signInSignUpRepo.getSignUpResponse();
    }

    public MutableLiveData<String> getUsernameExists() {
        return signInSignUpRepo.getUsernameExists();
    }

    public void signOut() {
        signInSignUpRepo.signOut();
    }

    /* Method used for actual registration of the user */
    public void register(String username, String email, String password, String shortDesc, Uri image) {
        User user = new User(email, username, password, shortDesc, "");
        signInSignUpRepo.signUp(user, image);
    }
}
