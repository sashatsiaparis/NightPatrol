package com.example.nightpatrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nightpatrol.api.model.PasswordChange;
import com.example.nightpatrol.api.model.Schedule;
import com.example.nightpatrol.api.model.Shift;
import com.example.nightpatrol.api.model.ShiftID;
import com.example.nightpatrol.api.service.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LandingScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShiftAdapter adapter;
    private String BASE_URL = "https://us-central1-vinnies-api-staging.cloudfunctions.net/api/";
    public String mTOKEN;
    private String TAG = "Jarrad sucks";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        mTOKEN = getIntent().getStringExtra("token");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:

                        break;

                    case R.id.nav_availability:
                        Intent intentAvailability = new Intent(LandingScreen.this, AvailabilityScreen.class);
                        intentAvailability.putExtra("token", mTOKEN);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_contacts:
                        Intent intentContacts = new Intent(LandingScreen.this, ContactUs.class);
                        intentContacts.putExtra("token", mTOKEN);
                        startActivity(intentContacts);
                        break;

                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestShifts();

    }

    private void requestShifts() {

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

        final ApiInterface apiInterface = build.create(ApiInterface.class);

        Call callFirst = apiInterface.getShifts();

        callFirst.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(final Call<List<Shift>> call, Response<List<Shift>> response) {

                int statusCode = response.code();
                final List<Shift> shifts_list = response.body();

                Log.d(TAG, Integer.toString(statusCode));
                if (statusCode == 200) {

                    adapter = new ShiftAdapter(shifts_list);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new ShiftAdapter.OnItemClickListener() {
                        @Override
                        public void onDeleteClick(final int position) {
                            final String shiftID = shifts_list.get(position).getId();
                            Log.d(TAG, shiftID);

                            Interceptor interceptor = new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + mTOKEN).build();
                                    return chain.proceed(newRequest);
                                }
                            };

                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                            builder.interceptors().add(interceptor);
                            OkHttpClient client = builder.build();

                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();

                            Retrofit builder2 = new Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .client(client)
                                    .build();

                            ApiInterface apiService2 = builder2.create(ApiInterface.class);

                            Call<String> shiftIDCall = apiService2.cancelShift(shiftID);

                            shiftIDCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    int statusCode = response.code();

                                    Log.d(TAG, Integer.toString(statusCode));
                                    Log.d(TAG, response.raw().toString());

                                    if (statusCode == 200) {
                                        Toast.makeText(LandingScreen.this, "You yeeted your shift", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d(TAG, t.toString());
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Shift>> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.e(TAG, "help me more", t);
                }

                Log.d(TAG, t.toString());

            }
        });
    }
}