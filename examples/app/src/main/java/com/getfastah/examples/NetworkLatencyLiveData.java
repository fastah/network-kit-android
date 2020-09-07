package com.getfastah.examples;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.getfastah.networkkit.MeasureManager;
import com.getfastah.networkkit.MeasureSample;

public class NetworkLatencyLiveData extends MutableLiveData<MeasureSample> {
    private MeasureManager fastahNetworkManager;
    private Context context;
    private MeasureManager.MeasurementCompletedListener listener = new MeasureManager.MeasurementCompletedListener() {
        @Override
        public void onMeasurementComplete(MeasureSample measureSample) {
            Log.d("FastahDemo", "New network sample = " + measureSample.toString());
            setValue(measureSample);
        }
    };

    public NetworkLatencyLiveData(Context ctx, MeasureSample value) {
        super(value);
        context = ctx;
        fastahNetworkManager = MeasureManager.getInstance(ctx);
    }

    public NetworkLatencyLiveData(Context ctx) {
        super();
        context = ctx;
        fastahNetworkManager = MeasureManager.getInstance(ctx);
    }

    @Override
    public void setValue(MeasureSample value) {
        super.setValue(value);
    }

    @Override
    protected void onActive() {
        super.onActive();
        fastahNetworkManager.register(listener);
        fastahNetworkManager.measureOnce(context);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        fastahNetworkManager.deregister(listener);
    }
}
