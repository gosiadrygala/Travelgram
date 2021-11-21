package com.example.travelgram.Views;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SignInSignUpVM signInSignUpVM;
    NavController navController;
    Toolbar toolbar;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //bottomNavigationView.setSelectedItemId(R.id.page_1);

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        checkIfSignedIn();
        toolbar = findViewById(R.id.topAppBar);
        //toolbar.setNavigationOnClickListener(v -> signInSignUpVM.signOut());
        //navController = Navigation.findNavController(this, R.id.fragmentContainerView);


        // eed = findViewById(R.id.feed);
        //feed.setOnClickListener(v-> navController.navigate(R.id.feedFragment));
        //buttonFragmentTwo.setOnClickListener(v-> navController.navigate(R.id.fragmentTwo));
        setupNavigation();
    }

    private void checkIfSignedIn() {
        signInSignUpVM.getCurrentUser().observe(this, user -> {
            if(user == null) {
                startActivity(new Intent(this, SignInActivity.class));
            }
        });
    }

    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        //setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.feed,
                R.id.map)
                .build();

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}