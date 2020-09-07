package com.getfastah.examples.ui.home;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.getfastah.examples.LocationLiveData;

public class HomeViewModel extends AndroidViewModel {

    private LocationLiveData mLocationLive;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mLocationLive = new LocationLiveData(application.getApplicationContext());
    }

    public LiveData<Location> getLocationData() {
        return mLocationLive;
    }
}