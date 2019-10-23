package com.example.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import com.example.nightpatrol.api.model.ShiftDetails;
import com.example.nightpatrol.api.service.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactUs extends AppCompatActivity {

    private String BASE_URL = "https://us-central1-vinnies-api-staging.cloudfunctions.net/api/";
    public String mTOKEN;
    private String TAG = "Testing";
    private String shiftID;
    public TextView name, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        mTOKEN = getIntent().getStringExtra("token");
        shiftID = getIntent().getStringExtra("id");
        Log.d(TAG, "" + mTOKEN);

        name = findViewById(R.id.nameText);
        phone = findViewById(R.id.phoneText);
        email = findViewById(R.id.emailText);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intentHome= new Intent(ContactUs.this, LandingScreen.class);
                        intentHome.putExtra("token", mTOKEN);
                        startActivity(intentHome);
                        break;

                    case R.id.nav_availability:

                        Intent intentAvailability = new Intent(ContactUs.this, AvailabilityScreen.class);
                        intentAvailability.putExtra("token", mTOKEN);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_contacts:

                        break;

                }
                return false;
            }
        });

        requestContact();

    }

    private void requestContact() {

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

        Call<ShiftDetails> call = apiInterface.getShiftDetails("XaNu4EgyDm5YGfcHhop9");

        Log.d(TAG, shiftID +"");

        call.enqueue(new Callback<ShiftDetails>() {
            @Override
            public void onResponse(Call<ShiftDetails> call, Response<ShiftDetails> response) {

                int statusCode = response.code();
                Log.d(TAG, Integer.toString(statusCode));
                Log.d(TAG, mTOKEN);

                if (statusCode == 200) {
                    String fullName = response.body().getShiftLeader().getFirstName();
                    String leaderMail = response.body().getShiftLeader().getEmail();
                    String leaderPhone = response.body().getShiftLeader().getPhone();

                    name.setText(fullName);
                    email.setText(leaderMail);
                    phone.setText(leaderPhone);
                }


            }

            @Override
            public void onFailure(Call<ShiftDetails> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

    }

}
