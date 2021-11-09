package com.example.travelgram.ViewModels.SignInSignUpVM;

import androidx.lifecycle.ViewModel;

public class SignInSignUpVM extends ViewModel {

    public SignInSignUpVM() {

    }

    public String signIn(String email, String password) {

        return "true";
    }

    public String signUp(String username, String email, String password, String shortDesc) {

        return "true";
    }
}
