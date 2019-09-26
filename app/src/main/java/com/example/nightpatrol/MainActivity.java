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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://us-central1-vinnies-api-staging.cloudfunctions.net/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    EditText email;
    EditText password;

    String inputEmail;
    String inputPassword;

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

                        Log.d("JARRADFYCJS", response.toString());

                        /*Intent intent = new Intent(v.getContext(), LandingScreen.class);
                        #startActivity(intent);*/
                    }
                    else {
                        Toast.makeText(MainActivity.this, "incorrect login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
                }
            });


        }
    };
}
