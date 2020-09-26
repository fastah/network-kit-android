package com.getfastah.examples;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.getfastah.networkkit.MeasureManager;
import com.getfastah.networkkit.MeasureSample;

public class NetworkLatencyLiveData extends MutableLiveData<MeasureSample> {

    private static final String TAG = "NetworkLatencyLiveData";

    private MeasureManager fastahNetworkManager;
    private Context context;
    private static final long NETWORK_MEASURE_INTERVAL = 10000; // 10 secs
    private MeasureManager.MeasurementCompletedListener listener = new MeasureManager.MeasurementCompletedListener() {
        @Override
        public void onMeasurementComplete(MeasureSample measureSample) {
            Log.d(TAG, "New network sample = " + measureSample.toString());
            setValue(measureSample);
        }
    };
    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Measuring Network Performance");
            fastahNetworkManager.measureOnce(context);
            mHandler.postDelayed(this, NETWORK_MEASURE_INTERVAL);
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
        mHandler.post(mRunnable);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        fastahNetworkManager.deregister(listener);
        mHandler.removeCallbacks(mRunnable);
    }
}
