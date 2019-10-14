package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

public class AvailabilityScreen extends AppCompatActivity {
    Switch sw1, sw2, sw3, sw4, sw5, sw6, sw7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_screen);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        sw1 = findViewById(R.id.switchMonday);
        sw2 = findViewById(R.id.switchTuesday);
        sw3 = findViewById(R.id.switchWednesday);
        sw4 = findViewById(R.id.switchThursday);
        sw5 = findViewById(R.id.switchFriday);
        sw6 = findViewById(R.id.switchSaturday);
        sw7 = findViewById(R.id.switchSunday);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intentAvailability = new Intent(AvailabilityScreen.this, LandingScreen.class);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_availability:

                        break;

                    case R.id.nav_contacts:
                        Intent intentContacts = new Intent(AvailabilityScreen.this, ContactUs.class);
                        startActivity(intentContacts);
                        break;

                }
                return false;
            }
        });
    }

    private View.OnClickListener saveViewListener = new View.OnClickListener() {

        public void onClick(View v) {

            Intent intent = new Intent(AvailabilityScreen.this, LandingScreen.class);
            intent.putExtra("switch1", sw1.isChecked());
            intent.putExtra("switch2", sw2.isChecked());
            intent.putExtra("switch3", sw3.isChecked());
            intent.putExtra("switch4", sw4.isChecked());
            intent.putExtra("switch5", sw5.isChecked());
            intent.putExtra("switch6", sw6.isChecked());
            intent.putExtra("switch7", sw7.isChecked());

            startActivity(intent);
        }
    };

}
