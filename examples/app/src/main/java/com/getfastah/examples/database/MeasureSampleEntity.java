package com.getfastah.examples.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "measure_sample_history")
public class MeasureSampleEntity {
    @PrimaryKey(autoGenerate = true)
    private long entryId;
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    @ColumnInfo(name = "latency")
    private long latency;
    @ColumnInfo(name = "networkName")
    private String networkName;
    @ColumnInfo(name = "networkType")
    private String networkType;

    public MeasureSampleEntity(long entryId, long timestamp, long latency, String networkName, String networkType) {
        this.entryId = entryId;
        this.timestamp = timestamp;
        this.latency = latency;
        this.networkName = networkName;
        this.networkType = networkType;
    }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }
}
