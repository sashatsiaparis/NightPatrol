package com.vinniesnp.nightpatrol;

import android.content.Intent;
import android.os.Bundle;

import com.vinniesnp.nightpatrol.api.model.ShiftDetails;
import com.vinniesnp.nightpatrol.api.model.ShiftUsers;
import com.vinniesnp.nightpatrol.api.service.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.MenuItem;

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

public class ContactTeamLeader extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private String BASE_URL = "https://us-central1-vinnies-api-staging.cloudfunctions.net/api/";
    public String mTOKEN;
    private String shiftID;
    public String userType;

    private String TAG = "Jarrad sucks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_team_leader);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        mTOKEN = getIntent().getStringExtra("token");
        shiftID = getIntent().getStringExtra("id");
        userType =  getIntent().getStringExtra("type");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intentContacts = new Intent(ContactTeamLeader.this, LandingScreen.class);
                        intentContacts.putExtra("token", mTOKEN);
                        startActivity(intentContacts);
                        break;

                    case R.id.nav_availability:
                        Intent intentAvailability = new Intent(ContactTeamLeader.this, AvailabilityScreen.class);
                        intentAvailability.putExtra("token", mTOKEN);
                        intentAvailability.putExtra("id", shiftID);
                        intentAvailability.putExtra("type", userType);
                        startActivity(intentAvailability);
                        break;

                    case R.id.nav_contacts:

                        break;

                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestContacts();

    }


    private void requestContacts() {

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

        Call<ShiftDetails> callFirst = apiInterface.getShiftDetails(shiftID);

        callFirst.enqueue(new Callback<ShiftDetails>() {
            @Override
            public void onResponse(Call<ShiftDetails> call, Response<ShiftDetails> response) {
                int statusCode = response.code();
                Log.d(TAG, Integer.toString(statusCode));
                Log.d(TAG, mTOKEN);

                if (statusCode == 200) {

                    final List<ShiftUsers> user_list = response.body().getShiftUsers();

                    user_list.add(addVinniesContact());
                    user_list.add(addHelplinet());

                    adapter = new ContactAdapter(user_list);

                    recyclerView.setAdapter(adapter);

                    /*String fullName = response.body().getShiftDriver().getFullName();
                    String leaderMail = response.body().getShiftDriver().getEmail();
                    String leaderPhone = response.body().getShiftDriver().getPhone();

                    name.setText(fullName);
                    email.setText(leaderMail);
                    phone.setText(leaderPhone);*/

                }
            }

            @Override
            public void onFailure(Call<ShiftDetails> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });

    }

    public ShiftUsers addVinniesContact() {
        ShiftUsers vinniesContact = new ShiftUsers();

        vinniesContact.setFirstName("Vinnies");
        vinniesContact.setLastName("Support Contacts");
        vinniesContact.setPhone("610382357923");
        vinniesContact.setEmail("Vinnies@vinnies");

        return vinniesContact;
    }

    public ShiftUsers addHelplinet() {
        ShiftUsers helplineContact = new ShiftUsers();

        helplineContact.setFirstName("Helpline");
        helplineContact.setLastName("Contacts");
        helplineContact.setPhone("911");
        helplineContact.setEmail("ACT@Police");

        return helplineContact;
    }
}


