package com.getfastah.examples.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.getfastah.examples.NetworkLatencyLiveData
import com.getfastah.examples.database.FastahDatabase
import com.getfastah.examples.database.MeasureSampleEntity
import com.getfastah.networkkit.MeasureSample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(application: Application, private val fastahDatabase: FastahDatabase) : AndroidViewModel(application) {
    val networkLatencyData: NetworkLatencyLiveData = NetworkLatencyLiveData(application.applicationContext)

    val measureSampleHistory = fastahDatabase.measureSampleDao.latestMeasurements

    class Factory(private val application: Application, private val fastahDatabase: FastahDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DashboardViewModel(application, fastahDatabase) as T
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