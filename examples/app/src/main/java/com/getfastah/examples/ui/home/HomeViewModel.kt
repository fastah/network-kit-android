package com.getfastah.examples.ui.home

import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import com.getfastah.examples.LocationLiveData
import com.getfastah.examples.NetworkLatencyLiveData
import com.getfastah.examples.database.FastahDatabase
import com.getfastah.examples.database.MeasureSampleEntity
import com.getfastah.examples.ui.dashboard.DashboardViewModel
import com.getfastah.networkkit.MeasureSample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application, private val fastahDatabase: FastahDatabase) : AndroidViewModel(application) {

    private val mLocationLive: LocationLiveData = LocationLiveData(application.applicationContext)
    val networkLatencyData: NetworkLatencyLiveData = NetworkLatencyLiveData(application.applicationContext)

    val locationData: LocationLiveData
        get() = mLocationLive

    class Factory(private val application: Application, private val fastahDatabase: FastahDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(application, fastahDatabase) as T
        }
    }

    fun persistMeasurement(measureSample: MeasureSample) {
        viewModelScope.launch() {
            val entity = MeasureSampleEntity(0, measureSample.timestamp, measureSample.latency, measureSample.networkName, measureSample.networkType, measureSample.networkState.name)
            withContext(Dispatchers.IO) {
                fastahDatabase.measureSampleDao.insert(entity)
            }
        }
    }
}