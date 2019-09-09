package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonPass = findViewById(R.id.buttonPass);

        buttonPass.setOnClickListener(buttonPassListener);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_home:

                        break;

                    case R.id.nav_availability:
                        Intent intentAvailaibility = new Intent(LandingScreen.this, AvailabilityScreen.class);
                        startActivity(intentAvailaibility);
                        break;

                    case R.id.nav_contacts:
                        Intent intentContacts = new Intent(LandingScreen.this, AvailabilityScreen.class);
                        startActivity(intentContacts);
                        break;

                }
                return false;
            }
        });

    }

    private View.OnClickListener buttonPassListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AvailabilityScreen.class);
            startActivity(intent);
        }
    };
}

