package com.example.nightpatrol;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nightpatrol.api.model.Login;
import com.example.nightpatrol.api.model.User;
import com.example.nightpatrol.api.service.UserClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://us-central1-vinnies-api-staging.cloudfunctions.net/api/")
            .addConverterFactory(GsonConverterFactory.create(gson));

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    EditText email;
    EditText password;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loginView = findViewById(R.id.loginView);

        loginView.setOnClickListener(loginViewListener);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
    }

    private View.OnClickListener loginViewListener = new View.OnClickListener() {
        public void onClick(final View v) {

            Login login = new Login();
            login.setEmail(email.getText().toString());
            login.setPassword(password.getText().toString());

            Call<User> call = userClient.getUser(login.getEmail(), login.getPassword());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {


                        Toast.makeText(MainActivity.this, "we good", Toast.LENGTH_SHORT).show();

                        User token = new User();
                        token.setToken(response.body().toString());
                        Log.e("JARRADFYCJS", token.getToken());
//                        Intent intent = new Intent(v.getContext(), LandingScreen.class);
//                        startActivity(intent);
                    }
                    else {
                        Log.e(TAG,"help me incorrect");
                        Toast.makeText(MainActivity.this, "incorrect login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    if( t instanceof IOException) {
                        Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"help",t);

                    } else {
                        Toast.makeText(MainActivity.this, "conversion issue", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"help me more",t);
                    }
                }
            });


        }
    };
}
