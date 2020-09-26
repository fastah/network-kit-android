package com.getfastah.examples.database;

import com.getfastah.networkkit.MeasureSample;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MeasureSampleDao {

    @Insert
    void insert(MeasureSampleEntity measureSampleEntity);

    @Query("Select * from measure_sample_history order by timestamp DESC limit 20")
    List<MeasureSampleEntity> getLatestMeasurements();
}
