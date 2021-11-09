package com.example.travelgram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button buttonFragmentOne = findViewById();
        //Button buttonSignUp = findViewById(R.id.signUpBtn);
        //NavController navController = Navigation.findNavController(this, R.id.fragmentSignIn);

        //buttonFragmentOne.setOnClickListener(v -> navController.navigate(R.id.fragmentOne));
        //buttonSignUp.setOnClickListener(v -> System.out.println("pickha tasuhdaosjdas"));
    }

    public void SignUpBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void SignInBtnClick(View view) {

    }
}