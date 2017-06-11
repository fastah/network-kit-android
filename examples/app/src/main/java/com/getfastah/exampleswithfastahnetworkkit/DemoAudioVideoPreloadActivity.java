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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.getfastah.networkkit.ConnectionQuality;
import com.getfastah.networkkit.NetworkAwareVideoPreloader;
import com.getfastah.networkkit.testapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DemoAudioVideoPreloadActivity extends AppCompatActivity {

    private static final String TAG = "FastahPreloaderApp";
    private NetworkAwareVideoPreloader.Listener mVideoPrefetchListener; // <--- add this as a member variable
    private Timer mNetworkCheckRepeater = new Timer();
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_audio_video_preload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Watching for video pre-load conditions...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // See explanations below.
                mTextView.setText("");
                initNetworkAwareVideoPrefetchHandler();
                doScheduleImmediateNetworkCheck();
            }
        });

        mTextView = (TextView)findViewById(R.id.largeTextView);
        mTextView.setText("");

         /* FASTAH NETWORK KIT - START */

        // a) Initialize  a listener object (saved as a member variable, mVideoPrefetchListener)
        //    that gets notified when network conditions are good for video pre-fetch
        initNetworkAwareVideoPrefetchHandler();

        // b) Schedule the network check right now
        doScheduleImmediateNetworkCheck();

        /* FASTAH NETWORK KIT - END */
    }

    /* FASTAH NETWORK KIT - START */
    private void initNetworkAwareVideoPrefetchHandler() {
        if ( mVideoPrefetchListener == null ) {
            mVideoPrefetchListener = new NetworkAwareVideoPreloader.Listener() {
                // This method gets woken up to do work
                @Override
                public void OnNetworkSuitableForCriteria(boolean isSuitable) {
                    Log.d(TAG, "Ok to trigger a video download now? :  " + isSuitable);
                    generateLogMessage(isSuitable);
                    if (isSuitable) {
                        mNetworkCheckRepeater.cancel();
                        // TODO: downloadAdVideosIfWeNeedAnyAndStorageSpaceAvailable();
                    } else {
                        // Check again later in the game-play
                        mNetworkCheckRepeater.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                doScheduleImmediateNetworkCheck();
                            }
                        }, TimeUnit.SECONDS.toMillis(30));
                    }
                }

                @Override
                public void OnNetworkTransitionOccurred(ConnectionQuality newQuality) {
                    // Kick off video download
                    Log.d(TAG, "Network transition OCCURRED to " + newQuality);
                    // TODO: downloadAdVideosIfWeNeedAnyAndStorageSpaceAvailable();
                }

                private void generateLogMessage(boolean isSuitable) {
                    final String timestamp = new SimpleDateFormat("h:mm:ss a", Locale.US).format(new Date());
                    final String logMessage = isSuitable ? (timestamp + " - GREAT TIME FOR VIDEO PRE-FETCH :) \n\n") : (timestamp + " - (no videos for you)\n\n");
                    mTextView.append(logMessage);
                }
            };
        }
    }

    private void doScheduleImmediateNetworkCheck() {
        // Check now, assuming it's a good time in the UX flow to do background downloads of video ads
        NetworkAwareVideoPreloader.getInstance().doScheduleImmediateNetworkCheck(
                getApplicationContext(),
                // This is the listener we created once for the activity.
                mVideoPrefetchListener,
                // Constraints on the network quality and cost when we need to download videos
                NetworkAwareVideoPreloader.Constraint.NETWORK_FAST_FOR_MEDIA_DOWNLOAD,
                NetworkAwareVideoPreloader.Constraint.NETWORK_DEVICE_CHARGING
        );
    }
    /* FASTAH NETWORK KIT - END */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo_audio_video_preload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // FASTAH NETWORK KIT - START
        Log.d(TAG, "Canceling watch for preload-friendly network conditions");
        NetworkAwareVideoPreloader.getInstance().deregisterListener(mVideoPrefetchListener);
        // FASTAH NETWORK KIT - END
    }
}
