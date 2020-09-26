package com.getfastah.examples.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = MeasureSampleEntity.class, version = 1, exportSchema = false)
public abstract class FastahDatabase extends RoomDatabase {

    private static FastahDatabase instance;

    public static synchronized FastahDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, FastahDatabase.class, "fastah-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract MeasureSampleDao getMeasureSampleDao();
}
