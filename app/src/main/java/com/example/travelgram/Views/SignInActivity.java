package com.example.travelgram.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelgram.Models.User;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private SignInSignUpVM signInSignUpVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //checkIfSignedIn();

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        signInSignUpVM.signOut();

        signInSignUpVM.getCurrentUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null) {
                    Intent intent = new Intent(SignInActivity.this, Feed.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //TODO toast for errors or u know

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("registerResponse"))
            Toast.makeText(this, bundle.getString("registerResponse"), Toast.LENGTH_LONG).show();
    }

    private void checkIfSignedIn() {
        signInSignUpVM.getCurrentUser().observe(this, user -> {
            if(user != null) {
                startActivity(new Intent(this, Feed.class));
            }
        });
    }

    public void SignUpBtnClick(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void SignInBtnClick(View view) {
        EditText email = findViewById(R.id.emailField);
        EditText password = findViewById(R.id.passwordField);
        String signInResponse = signInSignUpVM.signIn(email.getText().toString(), password.getText().toString());

        if(signInResponse.equals("true")) {
            Intent intent = new Intent(SignInActivity.this, Feed.class);
            startActivity(intent);
        }
        else Toast.makeText(this, signInResponse, Toast.LENGTH_LONG).show();
    }
}