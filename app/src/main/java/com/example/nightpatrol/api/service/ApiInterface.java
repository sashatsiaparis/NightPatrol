package com.example.nightpatrol.api.service;

import com.example.nightpatrol.api.model.CurrentUser;
import com.example.nightpatrol.api.model.PasswordChange;
import com.example.nightpatrol.api.model.Schedule;
import com.example.nightpatrol.api.model.Shift;
import com.example.nightpatrol.api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("auth")
    Call<User> getUser(
            @Query("email") String email,
            @Query("password") String password);

    @GET("shifts")
    Call<List<Shift>> getShifts();

    @GET("users/me")
    Call<CurrentUser> getCurrentUser();

    @POST("users/me/change-password")
    Call<PasswordChange> postPassword(
            @Body PasswordChange body);

    @PUT("users/id/{id}/schedule")
    Call<Schedule> setSchedule(
            @Path("id") String id,
            @Body Schedule body);
}
