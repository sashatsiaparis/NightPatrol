package com.example.nightpatrol.api.service;

import com.example.nightpatrol.api.model.PasswordChange;
import com.example.nightpatrol.api.model.Shift;
import com.example.nightpatrol.api.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("auth")
    Call<User> getUser(@Query("email") String email,
                       @Query("password") String password);

    @GET("shifts")
    Call<List<Shift>> getShifts();

    @POST("users/me/change-password")
    Call<PasswordChange> postPassword(@Body PasswordChange body);
}
