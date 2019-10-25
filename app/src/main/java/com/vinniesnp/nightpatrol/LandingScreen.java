package com.vinniesnp.nightpatrol;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vinniesnp.nightpatrol.api.model.CurrentUser;
import com.vinniesnp.nightpatrol.api.model.Shift;
import com.vinniesnp.nightpatrol.api.service.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class LandingScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShiftAdapter adapter;
    private String BASE_URL = "https://us-central1-vinnies-api-staging.cloudfunctions.net/api/";
    public String mTOKEN;
    private String TAG = "Jarrad sucks";
    private String userID;
    private String contactId;
    private String userType;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String teamId;


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
                        intentAvailability.putExtra("id", contactId);
                        intentAvailability.putExtra("type", userType);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_contacts:

                        if (userType.equals("1")) {
                            Intent intentContacts = new Intent(LandingScreen.this, ContactTeamLeader.class);
                            intentContacts.putExtra("token", mTOKEN);
                            intentContacts.putExtra("id", contactId);
                            intentContacts.putExtra("type", userType);
                            startActivity(intentContacts);
                            break;
                        } else {
                            Intent intentContacts = new Intent(LandingScreen.this, ContactUs.class);
                            intentContacts.putExtra("token", mTOKEN);
                            intentContacts.putExtra("id", contactId);
                            intentContacts.putExtra("type", userType);
                            startActivity(intentContacts);
                            break;
                        }

                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestShifts();

        getUserDetails();

        ImageView settingsImage = findViewById(R.id.imageSettings);
        settingsImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentSettings = new Intent(LandingScreen.this, Settings.class);
                intentSettings.putExtra("token", mTOKEN);
                intentSettings.putExtra("userId", userID);
                intentSettings.putExtra("firstName", firstName);
                intentSettings.putExtra("lastName", lastName);
                intentSettings.putExtra("email", email);
                intentSettings.putExtra("phone", phone);
                intentSettings.putExtra("teamId", teamId);
                startActivity(intentSettings);
            }
        });
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

                    setContactId(shifts_list.get(0).getId());

                    adapter.setOnItemClickListener(new ShiftAdapter.OnItemClickListener() {
                        @Override
                        public void onDeleteClick(final int position) {
                            final android.app.AlertDialog.Builder mBuilder = new AlertDialog.Builder(LandingScreen.this);
                            View mView = getLayoutInflater().inflate(R.layout.cancel_layout, null);
                            Button mDelete = (Button) mView.findViewById(R.id.deleteShiftButton);
                            Button mCancel = mView.findViewById(R.id.cancelButton);

                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();

                            mCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();

                                }
                            });

                            mDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


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
                                                Toast.makeText(LandingScreen.this, "You removed your shift", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d(TAG, t.toString());
                                        }
                                    });
                                    dialog.dismiss();
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
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

    public void setContactId(String i) {
        contactId = i;
    }

    public void getUserDetails() {

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

                if (statusCode == 200) {
                    Log.d(TAG, Integer.toString(statusCode));

                    userType = response.body().getType();
                    userID = response.body().getId();
                    firstName = response.body().getFirstName();
                    lastName = response.body().getLastName();
                    email = response.body().getEmail();
                    phone = response.body().getPhone();
                    teamId = response.body().getTeamId();
                    Log.e(TAG, userType);
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

}