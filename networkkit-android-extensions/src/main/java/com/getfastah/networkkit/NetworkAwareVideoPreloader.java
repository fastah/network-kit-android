/*
 * Copyright 2017, Blackbuck Computing Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.getfastah.networkkit;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.Keep;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class loads videos pre-emptively
 * and saves them to local storage in a fixed-size
 * disk cache for later playback in offline mode.
 */

public class NetworkAwareVideoPreloader {

    private static final String TAG = "FastahPreloader";
    private static NetworkAwareVideoPreloader mInstance = null;

    private ConcurrentHashMap<MeasureManager.MeasurementCompletedListener, Listener> mListenersImmediate = new ConcurrentHashMap<>();

    private NetworkAwareVideoPreloader() {
    }

    public synchronized static NetworkAwareVideoPreloader getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkAwareVideoPreloader();
        }
        return mInstance;
    }

    /**
     * This listener's method X gets invoked when network conditiosn change for the better
     * and are suitable for triggering a video download.
     *
     * @param listener An implementation of our listener interface
     */
    public static void registerForImprovedNetwork(Object listener) {

    }

    /**
     * This listener's method X gets invoked when the network available
     * is both operating fast enough for video downloads and is assumed to be
     * cheap from a cost-to-end-user perspective.
     *
     * @param listener An implementation of our listener interface
     */
    public static void registerForFastAndCheapNetwork(Object listener) {

    }

    public static void registerForNetworkAvailable(Listener listener) {
        //ConnectivityManager.addDefaultNetworkActiveListener(listener);
    }


    public static void registerForNetworkOutage(Listener listener) {

    }

    // Triggers a network test and returns if we can run a video download based on IMMEDIATE
    // network conditions
    public boolean doScheduleImmediateNetworkCheck(final Context ctx,
                                                   Listener userHandler,
                                                   final Constraint constraint1,
                                                   final Constraint constraint2) {

        if (userHandler == null) {
            Log.w(TAG, "Invalid argument: Listener object should NOT be null");
            return false;
        }

        MeasureManager.MeasurementCompletedListener internalHandler = null;
        boolean isUserHandlerAlreadyRegistered = false;

        for (MeasureManager.MeasurementCompletedListener mcl : mListenersImmediate.keySet()) {
            if (mListenersImmediate.get(mcl) == userHandler) {
                internalHandler = mcl;
                isUserHandlerAlreadyRegistered = true;
            }
        }

        if (!isUserHandlerAlreadyRegistered) {

            // Create a new internal measurement handler
             internalHandler = new MeasureManager.MeasurementCompletedListener() {
                @Override
                public void onMeasurementComplete(MeasureSample measureSample) {
                    boolean isItGoodForVideo = false;
                    Log.d(TAG, "Network sampling OK : " + measureSample.testOk
                                            + ", type = " + measureSample.networkType
                                            + ", name = " + measureSample.networkName
                                            + ", quality = " + measureSample.networkState);
                    // Rule 1
                    if (constraint1 == Constraint.NETWORK_FAST_FOR_MEDIA_DOWNLOAD) {
                        if (measureSample.testOk &&
                                (measureSample.networkState == ConnectionQuality.EXCELLENT || measureSample.networkState == ConnectionQuality.GOOD)) {
                            isItGoodForVideo = true;
                            Log.d(TAG, "Criteria1: Network conditions are good");
                        }
                    }
                    // Rule 2
                    if (constraint2 == Constraint.NETWORK_DEVICE_CHARGING) {
                        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                        Intent batteryStatus = ctx.registerReceiver(null, ifilter);
                        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                status == BatteryManager.BATTERY_STATUS_FULL;
                        isItGoodForVideo = isItGoodForVideo && isCharging;
                    }

                    Log.d(TAG, "Final determination: All criteria met? : " + isItGoodForVideo);

                    Listener userHandler = mListenersImmediate.get(this);
                    if (userHandler != null) {
                        try {
                            // Invoke the user's installed handler with the YES or NO answer
                            // WARN: This method may be slow/time-consuming.
                            userHandler.OnNetworkSuitableForCriteria(isItGoodForVideo);
                        } catch (Exception e) {
                            Log.w(TAG, e.getMessage());
                        }
                    }
                }
            };

            // Add it to the map
            mListenersImmediate.put(internalHandler, userHandler);
        }


        final MeasureManager manager = MeasureManager.getInstance(ctx);
        manager.register(internalHandler);
        Log.d(TAG, "Triggering network measurement");
        manager.measureOnce(ctx, "_VID");
        return true;
    }

    public void deregisterListener(Listener listener) {
        for (MeasureManager.MeasurementCompletedListener mcl : mListenersImmediate.keySet()) {
            if (mListenersImmediate.get(mcl) == listener) {
                mListenersImmediate.remove(mcl);
            }
        }
    }

    public enum Constraint {
        /** Good conditions for media downloads; latency and congestion are low. */
        NETWORK_FAST_FOR_MEDIA_DOWNLOAD,
        /** The device is charging or is fully charged */
        NETWORK_DEVICE_CHARGING, // Means GOOD or EXCELLENT +  COST is low
    }

    /**
     * The interface to implement for receiving OnNetworkTransitionOccurred events
     */
    @Keep
    public interface Listener {
        void OnNetworkTransitionOccurred(ConnectionQuality newQuality);

        void OnNetworkSuitableForCriteria(boolean isSuitable);
    }
}
