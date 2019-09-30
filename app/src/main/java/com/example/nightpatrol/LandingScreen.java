package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        ShiftData[] shiftData = new ShiftData[]{
                new ShiftData("March 1st","Northside","17:30","Team 03"),
                new ShiftData("March 22nd","Southside","21:30","Team 01"),
                new ShiftData("April 1st","Northside","17:30","Team 03"),
                new ShiftData("April 22nd","Southside","21:30","Team 01"),
                new ShiftData("May 1st","Southside","19:00","Team 01")
        };



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ShiftAdapter adapter = new ShiftAdapter(shiftData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:

                        break;

                    case R.id.nav_availability:
                        Intent intentAvailability = new Intent(LandingScreen.this, AvailabilityScreen.class);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_contacts:
                        Intent intentContacts = new Intent(LandingScreen.this, AvailabilityScreen.class);
                        startActivity(intentContacts);
                        break;

                }
                return false;
            }
        });


//        TextView textView3 = findViewById(R.id.textView3);
//        String nameString = getIntent().getStringExtra("tempname");
//        textView3.append(nameString);

    }
}


