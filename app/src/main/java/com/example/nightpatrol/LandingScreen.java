package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonPass = findViewById(R.id.buttonPass);
        TextView shiftText = findViewById(R.id.shiftsText);

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

        if(getIntent() != null) {

            boolean isChecked1 = getIntent().getBooleanExtra("switch1", false);
            boolean isChecked2 = getIntent().getBooleanExtra("switch2", false);
            boolean isChecked3 = getIntent().getBooleanExtra("switch3", false);
            boolean isChecked4 = getIntent().getBooleanExtra("switch4", false);
            boolean isChecked5 = getIntent().getBooleanExtra("switch5", false);
            boolean isChecked6 = getIntent().getBooleanExtra("switch6", false);
            boolean isChecked7 = getIntent().getBooleanExtra("switch7", false);

            shiftText.append(isChecked1 + " " + isChecked2 + " " + isChecked3 + " " + isChecked4 + " " + isChecked5 + " " + isChecked6 + " " + isChecked7);
        }

    }

    private View.OnClickListener buttonPassListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AvailabilityScreen.class);
            startActivity(intent);
        }
    };
}

