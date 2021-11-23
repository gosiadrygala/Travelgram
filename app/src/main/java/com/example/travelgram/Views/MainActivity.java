package com.example.travelgram.Views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.travelgram.R;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        signInSignUpVM = new ViewModelProvider(this).get(SignInSignUpVM.class);

        checkIfSignedIn();
        toolbar = findViewById(R.id.topAppBar);
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

        String email = signInSignUpVM.getCurrentUser().getValue().getEmail();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

            }
        });
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