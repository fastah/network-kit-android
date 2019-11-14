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

package com.getfastah.exampleswithfastahnetworkkit;

import android.os.Bundle;

import com.getfastah.networkkit.MeasureManager;
import com.getfastah.networkkit.MeasureSample;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.getfastah.networkkit.ConnectionQuality;
import com.getfastah.networkkit.testapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SampleActivity extends AppCompatActivity {

    private static final String TAG = "FastahSample";
    private Timer mNetworkCheckRepeater = new Timer();
    private TextView mTextView;
    private MeasureManager.MeasurementCompletedListener mNetworkMeasurementListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_monitor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Starting network measurement...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // See explanations below.
                mTextView.setText("");
            }
        });

        mTextView = findViewById(R.id.largeTextView);
        mTextView.setText("");

        /* FASTAH NETWORK KIT - START */

        // 1. Get a singleton instance of the Manager.
        MeasureManager fastahNetworkManager = MeasureManager.getInstance(getApplicationContext());
        Log.d(TAG, "Fastah Network Kit - initialized");

        // 2a. Initialize a listener that receives network measurements when they complete.
        mNetworkMeasurementListener = new MeasureManager.MeasurementCompletedListener() {
            @Override
            public void onMeasurementComplete(MeasureSample measureSample) {
                Log.d(TAG, "Fastah Network Kit - INCOMING: " + measureSample.toString());
                mTextView.append(measureSample.toString() + "\n");
            }
        };
        // 2b. Register the listener. It's a good idea to make a matching deregister() call in onDestroy()
        fastahNetworkManager.register(mNetworkMeasurementListener);
        Log.d(TAG, "Fastah Network Kit - registered a listener for measurements");


        // 3. Kick off a network measurement right now. The result gets reported inside the listener installed previously
        fastahNetworkManager.measureOnce(getApplicationContext());
        Log.d(TAG, "Fastah Network Kit - Requested a network measurement");

        /* FASTAH NETWORK KIT - END */
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // FASTAH NETWORK KIT - START
        Log.d(TAG, "De-register listener for Fastah Network Kit measurements");
        MeasureManager.getInstance(getApplicationContext()).deregister(mNetworkMeasurementListener);
        // FASTAH NETWORK KIT - END
    }
}
