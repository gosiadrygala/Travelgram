package com.example.travelgram.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;

public class SignUpActivity extends AppCompatActivity {

    private SignInSignUpVM signInSignUpVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);
    }

    public void BackToSignIn(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    public void Register(View view) {
        EditText username = findViewById(R.id.signUpNameField);
        EditText email = findViewById(R.id.signUpEmailField);
        EditText password = findViewById(R.id.signUpPassword);
        EditText shortDesc = findViewById(R.id.shortDescMultiFiled);

        String signUpResponse = signInSignUpVM.signUp(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                shortDesc.getText().toString());

        if(signUpResponse.equals("true")) {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            intent.putExtra("registerResponse", "Account created, sign in.");
            startActivity(intent);
        }
        else Toast.makeText(this, signUpResponse, Toast.LENGTH_LONG).show();
    }
}