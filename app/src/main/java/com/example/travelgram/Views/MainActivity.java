package com.example.travelgram.Views;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    SignInSignUpVM signInSignUpVM;
    NavController navController;
    MaterialToolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //bottomNavigationView.setSelectedItemId(R.id.page_1);

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        checkIfSignedIn();
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> signInSignUpVM.signOut());
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);


        // eed = findViewById(R.id.feed);
        //feed.setOnClickListener(v-> navController.navigate(R.id.feedFragment));
        //buttonFragmentTwo.setOnClickListener(v-> navController.navigate(R.id.fragmentTwo));
    }

    private void checkIfSignedIn() {
        signInSignUpVM.getCurrentUser().observe(this, user -> {
            if(user == null) {
                startActivity(new Intent(this, SignInActivity.class));
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feed:
                navController.navigate(R.id.feedFragment);
                return true;
            case R.id.page_2:
                System.out.println("asiudhaishudasd");
                return true;

            case R.id.page_3:
                navController.navigate(R.id.map);
                System.out.println("lalalla");
                return true;

            case R.id.page_4:
                System.out.println("asiudhaishudasd");
                return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}