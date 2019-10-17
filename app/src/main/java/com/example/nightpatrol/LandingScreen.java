package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nightpatrol.api.model.PasswordChange;
import com.example.nightpatrol.api.model.Shift;
import com.example.nightpatrol.api.service.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LandingScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShiftAdapter adapter ;
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        requestShifts();







        //adapter = new ShiftAdapter();


        //recyclerView.setAdapter(adapter);



        //Toast.makeText(LandingScreen.this, token, Toast.LENGTH_SHORT).show();

//        TextView textView3 = findViewById(R.id.textView3);
//        String nameString = getIntent().getStringExtra("tempname");
//        textView3.append(nameString);

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


        ApiInterface apiService2 = build.create(ApiInterface.class);

        Call call2 = apiService2.getShifts();

        call2.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(Call<List<Shift>> call, Response<List<Shift>> response) {

                int statusCode = response.code();
                Log.d(TAG, Integer.toString(statusCode));
                if (statusCode == 200) {

                    List<Shift> shifts_list = response.body();

                    adapter = new ShiftAdapter(shifts_list);
                    recyclerView.setAdapter(adapter);


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


    //ShiftData[] shiftData = new ShiftData[]{
    //        new ShiftData("March 1st","Northside","17:30","Team 03"),
    //        new ShiftData("March 22nd","Southside","21:30","Team 01"),
    //        new ShiftData("April 1st","Northside","17:30","Team 03"),
     //       new ShiftData("April 22nd","Southside","21:30","Team 01"),
    //        new ShiftData("May 1st","Southside","19:00","Team 01")
    //};


}


