package com.example.nightpatrol.api.service;

import com.example.nightpatrol.api.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserClient {
    @GET("auth")
    Call<User> getUser(@Query("email") String email,
                       @Query("password") String password);
}
