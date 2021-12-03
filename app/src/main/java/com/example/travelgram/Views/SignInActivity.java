package com.example.travelgram.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.google.firebase.auth.FirebaseUser;

/* Class managing the sign in activity */
public class SignInActivity extends AppCompatActivity {

    private SignInSignUpVM signInSignUpVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        observerForSignInResponse();
        checkIfSignedIn();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("registerResponse"))
            Toast.makeText(this, bundle.getString("registerResponse"), Toast.LENGTH_LONG).show();
    }

    /* Check whether the user is logged in, if yes, the user is redirected to the Main Activity */
    private void checkIfSignedIn() {
        signInSignUpVM.getCurrentUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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
        signInSignUpVM.signIn(email.getText().toString(), password.getText().toString());
    }

    /* Observing the response from the sign in action */
    private void observerForSignInResponse() {
        signInSignUpVM.getSignInResponse().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        makeToast(s);
                    }
                }
        );
    }

    private void makeToast(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }
}