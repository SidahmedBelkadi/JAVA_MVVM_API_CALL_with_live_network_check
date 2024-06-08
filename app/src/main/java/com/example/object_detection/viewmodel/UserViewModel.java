package com.example.object_detection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.object_detection.data.model.LoginRequest;
import com.example.object_detection.data.model.LoginResponse;
import com.example.object_detection.data.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<LoginResponse> loginResponse;

    public UserViewModel() {
        userRepository = new UserRepository();
        loginResponse = new MutableLiveData<>();
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    public void login(LoginRequest loginRequest) {
        loginResponse = (MutableLiveData<LoginResponse>) userRepository.login(loginRequest);
    }
}
