package com.example.nightpatrol.api.service;

import com.example.nightpatrol.api.model.Login;
import com.example.nightpatrol.api.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface UserClient {
    @GET("auth")
    Call<User>
}
