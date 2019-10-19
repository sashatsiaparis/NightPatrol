package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import com.example.nightpatrol.api.model.CurrentUser;
import com.example.nightpatrol.api.model.Schedule;
import com.example.nightpatrol.api.model.Shift;
import com.example.nightpatrol.api.service.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TabHost;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvailabilityScreen extends AppCompatActivity {
    public Switch switchMonday, switchTuesday, switchWednesday, switchThursday, switchFriday, switchSaturday, switchSunday;
    public boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    private String BASE_URL = "https://us-central1-vinnies-api-staging.cloudfunctions.net/api/";
    public String mTOKEN;
    private String TAG = "Jarrad sucks";
    public Button saveButton;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_screen);

        mTOKEN = getIntent().getStringExtra("token");

        switchMonday = findViewById(R.id.switchMonday);
        switchTuesday = findViewById(R.id.switchTuesday);
        switchWednesday = findViewById(R.id.switchWednesday);
        switchThursday = findViewById(R.id.switchThursday);
        switchFriday = findViewById(R.id.switchFriday);
        switchSaturday = findViewById(R.id.switchSaturday);
        switchSunday = findViewById(R.id.switchSunday);

        saveButton = findViewById(R.id.saveButton);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intentAvailability = new Intent(AvailabilityScreen.this, LandingScreen.class);
                        intentAvailability.putExtra("token", mTOKEN);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_availability:

                        break;

                    case R.id.nav_contacts:
                        Intent intentContacts = new Intent(AvailabilityScreen.this, ContactUs.class);
                        intentContacts.putExtra("token", mTOKEN);
                        startActivity(intentContacts);
                        break;

                }
                return false;
            }
        });

        getAvailability();
        saveSchedule();

    }

    private void getAvailability() {
        Interceptor authInterception = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + mTOKEN).build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(authInterception);
        OkHttpClient client = builder.build();

        Retrofit build = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        ApiInterface apiInterface = build.create(ApiInterface.class);

        Call<CurrentUser> call = apiInterface.getCurrentUser();

        call.enqueue(new Callback<CurrentUser>() {
            @Override
            public void onResponse(Call<CurrentUser> call, Response<CurrentUser> response) {

                int statusCode = response.code();
                Log.d(TAG, Integer.toString(statusCode));
                Log.d(TAG, mTOKEN);
                monday = response.body().getSchedule().getMonday();
                tuesday = response.body().getSchedule().getTuesday();
                wednesday = response.body().getSchedule().getWednesday();
                thursday = response.body().getSchedule().getThursday();
                friday = response.body().getSchedule().getFriday();
                saturday = response.body().getSchedule().getSaturday();
                sunday = response.body().getSchedule().getSunday();

                id = response.body().getId();

                if (statusCode == 200) {
                    Log.d(TAG, Integer.toString(statusCode));
                    Log.d(TAG, id);

                    switchMonday.setChecked(monday);
                    switchTuesday.setChecked(tuesday);
                    switchWednesday.setChecked(wednesday);
                    switchThursday.setChecked(thursday);
                    switchFriday.setChecked(friday);
                    switchSaturday.setChecked(saturday);
                    switchSunday.setChecked(sunday);

                }

            }

            @Override
            public void onFailure(Call<CurrentUser> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.e(TAG, "something happened", t);
                }
                Log.d(TAG, t.toString());
            }
        });
    }

    private void saveSchedule() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Interceptor authInterception = new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + mTOKEN).build();
                        return chain.proceed(newRequest);
                    }
                };

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.interceptors().add(authInterception);
                OkHttpClient client = builder.build();

                Retrofit build = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

                ApiInterface apiInterface = build.create(ApiInterface.class);

                Call<Schedule> call = apiInterface.setSchedule(id, new Schedule(switchMonday.isChecked(), switchTuesday.isChecked(), switchWednesday.isChecked(),
                        switchThursday.isChecked(), switchFriday.isChecked(), switchSaturday.isChecked(), switchSunday.isChecked()));

                call.enqueue(new Callback<Schedule>() {
                    @Override
                    public void onResponse(Call<Schedule> call, Response<Schedule> response) {

                        int statusCode = response.code();
                        Log.d(TAG, Integer.toString(statusCode));
                        Log.d(TAG, response.raw().toString());
                        Log.d(TAG, id);

                        if (statusCode == 200) {
                            Log.d(TAG, response.body().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<Schedule> call, Throwable t) {
                        if (t instanceof IOException) {
                            Log.e(TAG, "help me more", t);
                        }

                        Log.d(TAG, t.toString());
                    }
                });
            }
        });

    }
}

