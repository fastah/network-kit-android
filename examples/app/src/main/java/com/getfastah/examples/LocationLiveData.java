package com.getfastah.examples;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.TimeUnit;

public final class LocationLiveData extends MutableLiveData<Location> {

    private FusedLocationProviderClient locationProviderClient;
    private Context cachedCtx;

    /**
     * Creates a LiveData initialized with the given {@code value}.
     *
     * @param value initial value
     */
    public LocationLiveData(Context ctx, Location value) {
        super(value);
        cachedCtx = ctx;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
    }

    /**
     * Creates a LiveData with no value assigned to it.
     */
    public LocationLiveData(Context ctx) {
        super();
        cachedCtx = ctx;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
    }

    /**
     * Called when the number of active observers change to 0 from 1.
     * <p>
     * This callback can be used to know that this LiveData is being used thus should be kept
     * up to date.
     */
    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setLocation(location);
                }
            }
        });
        startLocationUpdates(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Called when the number of active observers change from 1 to 0.
     * <p>
     * This does not mean that there are no observers left, there may still be observers but their
     * lifecycle states aren't {@link Lifecycle.State#STARTED} or {@link Lifecycle.State#RESUMED}
     * (like an Activity in the back stack).
     * <p>
     * You can check if there are observers via {@link #hasObservers()}.
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        stopLocationUpdates();
    }

    private void setLocation(Location location) {
        setValue(location);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location loc : locationResult.getLocations()) {
                Log.d("FastahDemo", "Set location to : " + loc.getAccuracy() + ", lng = " + loc.getLongitude() + ", lat = " + loc.getLatitude());
                setLocation(loc);
            }
        }
    };

    private void startLocationUpdates(int locationRequestPriority) {
        if (ActivityCompat.checkSelfPermission(cachedCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(cachedCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            Log.d("FastahDemo", "START location updates");
            final LocationRequest lr = LocationRequest.create()
                    .setPriority(locationRequestPriority).setInterval(TimeUnit.SECONDS.toMillis(2));
            locationProviderClient.requestLocationUpdates(lr, locationCallback, null);
        } catch (Exception e) {
            Log.w("FastahDemo", "Unable to start location updates due to: " + e.getMessage());
        }
    }

    private void stopLocationUpdates() {
        if (locationProviderClient != null) {
            Log.d("FastahDemo", "STOP location updates");
            locationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

}
