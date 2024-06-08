package com.example.object_detection.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.object_detection.R;
import com.example.object_detection.data.model.LoginRequest;
import com.example.object_detection.data.model.LoginResponse;
import com.example.object_detection.data.network.NetworkUtils;
import com.example.object_detection.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView statusTextView;
    private ProgressBar progressBar;
    private TextView noInternetTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        statusTextView = findViewById(R.id.statusTextView);
        progressBar = findViewById(R.id.progressBar);
        noInternetTextView = findViewById(R.id.noInternetTextView);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        NetworkUtils.registerNetworkCallback(this);
        NetworkUtils.getNetworkStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    noInternetTextView.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                } else {
                    noInternetTextView.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill the form", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                userViewModel.login(new LoginRequest(email, password));
                userViewModel.getLoginResponse().observe(LoginActivity.this, new Observer<LoginResponse>() {
                    @Override
                    public void onChanged(LoginResponse loginResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (loginResponse != null && loginResponse.isSuccess()) {
                            statusTextView.setText("Login Successful");
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = (loginResponse != null && loginResponse.getMessage() != null) ? loginResponse.getMessage() : "An error occurred";
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            statusTextView.setText("Login Failed");
                        }
                    }
                });
            }
        });
    }
}
