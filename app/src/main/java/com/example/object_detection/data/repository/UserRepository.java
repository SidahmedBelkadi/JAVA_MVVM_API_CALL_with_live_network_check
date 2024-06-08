package com.example.object_detection.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.object_detection.data.model.ErrorResponse;
import com.example.object_detection.data.model.LoginRequest;
import com.example.object_detection.data.model.LoginResponse;
import com.example.object_detection.data.network.ApiService;
import com.example.object_detection.data.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getClient("http://192.168.1.8:8000").create(ApiService.class);
    }

    public LiveData<LoginResponse> login(LoginRequest loginRequest) {
        final MutableLiveData<LoginResponse> data = new MutableLiveData<>();

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(null);
                } else {
                    // Parse the error body
                    Gson gson = new Gson();
                    TypeAdapter<ErrorResponse> adapter = gson.getAdapter(ErrorResponse.class);
                    try {
                        if (response.errorBody() != null) {
                            ErrorResponse errorResponse = adapter.fromJson(response.errorBody().string());
                            LoginResponse loginResponse = new LoginResponse();
                            loginResponse.setSuccess(false);
                            loginResponse.setMessage(errorResponse.getMessage());
                            data.setValue(loginResponse);
                        } else {
                            LoginResponse loginResponse = new LoginResponse();
                            loginResponse.setSuccess(false);
                            loginResponse.setMessage("Unknown error occurred");
                            data.setValue(loginResponse);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.setSuccess(false);
                        loginResponse.setMessage("Error parsing error response");
                        data.setValue(loginResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse errorResponse = new LoginResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Failure: " + t.getMessage());
                data.setValue(errorResponse);
            }
        });
        return data;
    }
}
