package com.example.object_detection.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkUtils {
    private static MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void registerNetworkCallback(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        // Check initial network status
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isNetworkAvailable.postValue(activeNetworkInfo != null && activeNetworkInfo.isConnected());

        connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                isNetworkAvailable.postValue(true);
            }

            @Override
            public void onLost(Network network) {
                isNetworkAvailable.postValue(false);
            }
        });
    }

    public static LiveData<Boolean> getNetworkStatus() {
        return isNetworkAvailable;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
