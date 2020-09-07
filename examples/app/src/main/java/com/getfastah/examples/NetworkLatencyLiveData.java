package com.getfastah.examples;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.getfastah.networkkit.MeasureManager;
import com.getfastah.networkkit.MeasureSample;

public class NetworkLatencyLiveData extends MutableLiveData<MeasureSample> {
    private MeasureManager fastahNetworkManager;

    public NetworkLatencyLiveData(MeasureSample value) {
        super(value);
    }

    public NetworkLatencyLiveData() {
        super();
    }

    @Override
    public void setValue(MeasureSample value) {
        super.setValue(value);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }
}
