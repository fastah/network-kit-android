package com.getfastah.examples;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.getfastah.examples.database.FastahDatabase;

public class ExampleApplication extends Application {

    private FastahDatabase mFastahDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        mFastahDatabase = FastahDatabase.getInstance(this);
    }

    public FastahDatabase getFastahDatabase() {
        return mFastahDatabase;
    }
}
