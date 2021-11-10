package com.example.travelgram.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.travelgram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Feed extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.page_1);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_1:
                System.out.println("asiudhaishudasd");
                return true;

            case R.id.page_2:
                System.out.println("asiudhaishudasd");
                return true;

            case R.id.page_3:
                System.out.println("asiudhaishudasd");
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