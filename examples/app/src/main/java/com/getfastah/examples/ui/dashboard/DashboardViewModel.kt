package com.getfastah.examples.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.getfastah.examples.NetworkLatencyLiveData
import com.getfastah.examples.database.FastahDatabase
import com.getfastah.examples.database.MeasureSampleEntity
import com.getfastah.networkkit.MeasureSample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(application: Application, private val fastahDatabase: FastahDatabase) : AndroidViewModel(application) {

    val measureSampleHistory: LiveData<MutableList<MeasureSampleEntity>> = fastahDatabase.measureSampleDao.latestMeasurements

    class Factory(private val application: Application, private val fastahDatabase: FastahDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DashboardViewModel(application, fastahDatabase) as T
        }
    }
}