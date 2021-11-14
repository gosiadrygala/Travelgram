package com.example.travelgram.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;


public class SignUpActivity extends AppCompatActivity {

    private EditText username, email, password, shortDesc;
    private SignInSignUpVM signInSignUpVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        observeForExistingUsername();

        observerForSignUpResponse();
    }

    /* Method used for observing the sign up response */
    private void observerForSignUpResponse() {
        signInSignUpVM.getSignUpResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("Account created, please sign in.")) {
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    shortDesc.setText("");
                    makeToast(s);
                } else {
                    makeToast(s);
                }
                System.out.println(s);
            }
        }
    );
    }

    /* Method used for observing username exist response */
    private void observeForExistingUsername() {
        signInSignUpVM.getUsernameExists().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("username not found")) {
                    register();
                }
                else{
                    makeToast("Username already exists.");
                }
            }
        });
    }


    public void BackToSignIn(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void makeToast(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }

    /* Method used for validating all the fields from sign up form, including the check for
    * existing username */
    public void validateRegister(View view) {
        username = findViewById(R.id.signUpNameField);
        email = findViewById(R.id.signUpEmailField);
        password = findViewById(R.id.signUpPassword);
        shortDesc = findViewById(R.id.shortDescMultiFiled);

        String s = signInSignUpVM.validateFields(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                shortDesc.getText().toString());
        if(!s.equals("true")){
            makeToast(s);
        }
    }

    /* Method used for an actual registration of the user, after checking the fields and username */
    private void register() {
        username = findViewById(R.id.signUpNameField);
        email = findViewById(R.id.signUpEmailField);
        password = findViewById(R.id.signUpPassword);
        shortDesc = findViewById(R.id.shortDescMultiFiled);
        signInSignUpVM.register(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                shortDesc.getText().toString());
    }

}