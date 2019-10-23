package com.example.nightpatrol;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nightpatrol.api.model.Login;
import com.example.nightpatrol.api.model.PasswordChange;
import com.example.nightpatrol.api.model.User;
import com.example.nightpatrol.api.service.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public String BASE_URL = "https://us-central1-vinnies-api-staging.cloudfunctions.net/api/";

    private static final String TAG = MainActivity.class.getName();

    private EditText inputEmail;
    private EditText inputPassword;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loginView = findViewById(R.id.loginView);

        loginView.setOnClickListener(loginViewListener);
        inputEmail = findViewById(R.id.emailText);
        inputPassword = findViewById(R.id.passwordText);

    }

    private View.OnClickListener loginViewListener = new View.OnClickListener() {
        public void onClick(final View v) {

            Login login = new Login();
            login.setEmail(inputEmail.getText().toString());
            login.setPassword(inputPassword.getText().toString());

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

            Retrofit retrofit = builder.build();

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<User> call = apiInterface.getUser(login.getEmail(), login.getPassword());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    int statusCode = response.code();
                    final User token = response.body();

                    if (statusCode == 200) {
                        Log.d(TAG, token.getToken());
                        Log.d(TAG, token.getTemporaryStatus());

                        if (token.getTemporaryStatus().toLowerCase().equals("true")) {
                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                            final EditText mPassword1 = (EditText) mView.findViewById(R.id.password1);
                            final EditText mPassword2 = (EditText) mView.findViewById(R.id.password2);
                            Button mSave = (Button) mView.findViewById(R.id.deleteButton);
                            Button mCancel = (Button) mView.findViewById(R.id.cancelButton);

                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();

                            mSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final String mPasswordOne = mPassword1.getText().toString();
                                    final String mPasswordTwo = mPassword2.getText().toString();

                                    if ((!mPasswordOne.isEmpty() && !mPasswordTwo.isEmpty()) || (!mPasswordOne.isEmpty() || !mPasswordTwo.isEmpty())) {
                                        if (mPasswordOne.equals(mPasswordTwo)) {
                                            if (!PASSWORD_PATTERN.matcher(mPasswordOne).matches() || !PASSWORD_PATTERN.matcher(mPasswordTwo).matches()) {
                                                mPassword1.setError("Password does not meet standards.");
                                            } else {

                                                Interceptor interceptor = new Interceptor() {
                                                    @Override
                                                    public okhttp3.Response intercept(Chain chain) throws IOException {
                                                        Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token.getToken()).build();
                                                        return chain.proceed(newRequest);
                                                    }
                                                };

                                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                                builder.interceptors().add(interceptor);
                                                OkHttpClient client = builder.build();

                                                Retrofit builder2 = new Retrofit.Builder()
                                                        .baseUrl(BASE_URL)
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .client(client)
                                                        .build();

                                                ApiInterface apiService2 = builder2.create(ApiInterface.class);

                                                Call<PasswordChange> call2 = apiService2.postPassword(new PasswordChange(mPassword1.getText().toString()));

                                                call2.enqueue(new Callback<PasswordChange>() {
                                                    @Override
                                                    public void onResponse(Call<PasswordChange> call, Response<PasswordChange> response) {

                                                        int statusCode = response.code();

                                                        Log.d(TAG, Integer.toString(statusCode));

                                                        if (statusCode == 200) {
                                                            Toast.makeText(MainActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(MainActivity.this, LandingScreen.class);
                                                            intent.putExtra("token", token.getToken());
                                                            startActivity(intent);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PasswordChange> call, Throwable t) {
                                                        if (t instanceof IOException) {
                                                            Toast.makeText(MainActivity.this, "Conversion issue, please contact the developer.", Toast.LENGTH_SHORT).show();
                                                            Log.e(TAG, "help me more", t);
                                                        }

                                                        Log.d(TAG, t.toString());

                                                    }
                                                });
                                            }
                                        } else {
                                            mPassword1.setError(null);
                                            mPassword2.setError("Password does not match");
                                        }

                                    } else {
                                        mPassword1.setError("Field can't be empty");
                                        mPassword2.setError("Field can't be empty");
                                    }
                                }
                            });

                            mCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LandingScreen.class);
                            intent.putExtra("token", token.getToken());
                            startActivity(intent);
                        }
                    } else {
                        Log.e(TAG, "Incorrect Login");
                        Toast.makeText(MainActivity.this, "Wrong username or inputPassword", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(MainActivity.this, "This is an actual network failure, please contact the developer.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "help", t);

                    } else {
                        Toast.makeText(MainActivity.this, "Conversion issue, please contact the developer.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "help me more", t);
                    }
                }
            });
        }
    };

}
