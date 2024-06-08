package com.example.object_detection.data.network;

import com.example.object_detection.data.model.LoginRequest;
import com.example.object_detection.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/v1/login")
    @Headers({"Accept:application/json", "Content-Type:application/json"})
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
