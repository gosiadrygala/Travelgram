package com.example.travelgram.Views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {

    private EditText username, email, password, shortDesc;
    private SignInSignUpVM signInSignUpVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        signInSignUpVM.getResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("Account created, please sign in.")) {
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    shortDesc.setText("");
                    extracted(s);
                } else {
                    extracted(s);
                }
                System.out.println(s);
            }
        }
    );
    }

    public void BackToSignIn(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void extracted(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }

    public void Register(View view) {
        username = findViewById(R.id.signUpNameField);
        email = findViewById(R.id.signUpEmailField);
        password = findViewById(R.id.signUpPassword);
        shortDesc = findViewById(R.id.shortDescMultiFiled);

        String s = signInSignUpVM.signUp(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                shortDesc.getText().toString());

        //TODO add picture in user registration
    }
}