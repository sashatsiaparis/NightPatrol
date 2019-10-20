package com.example.nightpatrol.api.service;

import com.example.nightpatrol.api.model.CurrentUser;
import com.example.nightpatrol.api.model.PasswordChange;
import com.example.nightpatrol.api.model.Schedule;
import com.example.nightpatrol.api.model.Shift;
import com.example.nightpatrol.api.model.ShiftID;
import com.example.nightpatrol.api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //Used when logging in, this gets the users token
    @GET("auth")
    Call<User> getUser(
            @Query("email") String email,
            @Query("password") String password);

    //Used on Landing Screen, this lifts the shifts
    @GET("shifts")
    Call<List<Shift>> getShifts();

    //Gets the information for the current user, including their availability.
    @GET("users/me")
    Call<CurrentUser> getCurrentUser();

    //Call to change the users password. Used when isTemporary is set to true.
    @POST("users/me/change-password")
    Call<PasswordChange> postPassword(
            @Body PasswordChange body);

    //Call to change the users availability.
    @PUT("users/me/schedule")
    Call<Schedule> setSchedule(
            //@Path("id") String id,
            @Body Schedule body);

    //Call to cancel an assigned shift.
    @POST("shifts/{id}/cancel")
    Call<String> cancelShift(
            @Path("id") String id);
}
