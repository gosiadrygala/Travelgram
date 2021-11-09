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

public class SignInActivity extends AppCompatActivity {

    private SignInSignUpVM signInSignUpVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        //TODO toast for errors or u know

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("registerResponse"))
            Toast.makeText(this, bundle.getString("registerResponse"), Toast.LENGTH_LONG).show();
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