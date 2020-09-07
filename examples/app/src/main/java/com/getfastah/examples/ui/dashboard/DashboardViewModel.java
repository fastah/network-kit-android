package com.getfastah.examples.ui.dashboard;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.getfastah.examples.LocationLiveData;
import com.getfastah.examples.NetworkLatencyLiveData;

public class DashboardViewModel extends AndroidViewModel {

    private NetworkLatencyLiveData mLatencyLive;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        mLatencyLive = new NetworkLatencyLiveData(application.getApplicationContext());
    }

    public NetworkLatencyLiveData getNetworkLatencyData() {
        return mLatencyLive;
    }
}