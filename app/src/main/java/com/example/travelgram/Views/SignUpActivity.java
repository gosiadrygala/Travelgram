package com.example.travelgram.Views;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;


public class SignUpActivity extends AppCompatActivity {

    private EditText username, email, password, shortDesc;
    private SignInSignUpVM signInSignUpVM;
    private ImageButton imageButton;
    private RadioButton radioButton;
    private static final int PICK_FROM_GALLERY = 1;
    private Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        imageButton = findViewById(R.id.uploadPictureSignUp);
        radioButton = findViewById(R.id.radioButtonSingUp);
        imageButton.setOnClickListener(b -> {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PICK_FROM_GALLERY);
                } else {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(gallery);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
                shortDesc.getText().toString(),
                image);
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
                shortDesc.getText().toString(),
                image);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(gallery);
                } else {
                    Toast.makeText(this, "To create profile you need to upload an image  " +
                            "and need to enable the access to your files in settings of your device.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    image = result.getData().getData();
                    radioButton.setChecked(true);
                }
            });
}