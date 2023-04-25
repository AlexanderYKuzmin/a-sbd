package com.example.a_sbd.domain.usecases

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.a_sbd.data.workers.BleScanWorker
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.ui.MainActivity.Companion.IS_SCAN_START
import com.example.a_sbd.ui.MainActivity.Companion.SCANNING_DATA
import com.example.a_sbd.ui.MainActivity.Companion.SCANNING_WORK
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ScanUseCase @Inject constructor (
    private val application: Application,
) {

    /*private val _devicesSimple = MutableLiveData<List<DeviceSimple>>()
        val devicesSimple: LiveData<List<DeviceSimple>>
        get() = _devicesSimple*/

    @SuppressLint("MissingPermission")
    operator fun invoke(isScanStart: Boolean){
        Log.d(TAG, "Invoke scan use case")

        val data = Data.Builder()
            .putBoolean(IS_SCAN_START, isScanStart)
            .build()


        val scanRequest = OneTimeWorkRequest.Builder(BleScanWorker::class.java)
            //.setId(UUID.fromString(SCAN_ID))
            .setInputData(data)
            .build()

        WorkManager.getInstance(application).enqueueUniqueWork(
            SCANNING_WORK, ExistingWorkPolicy.REPLACE, scanRequest)

        //WorkManager.getInstance(application).enqueue(scanRequest)
    }
}