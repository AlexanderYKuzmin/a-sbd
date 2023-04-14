package com.example.a_sbd.domain.usecases

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a_sbd.data.bluetooth.BleScanCallback
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ScanUseCase (
    private val application: Application,
    /*private val bleScanner: BluetoothLeScanner,
    private val settings: ScanSettings,
    private val bleScanCallback: BleScanCallback*/
) {

    private val _devicesSimple = MutableLiveData<List<DeviceSimple>>()
        val devicesSimple: LiveData<List<DeviceSimple>>
        get() = _devicesSimple

    /*@SuppressLint("MissingPermission")
    operator fun invoke(){
        Log.d(TAG, "Invoke")
        startScan()

        *//*val scanRequest = OneTimeWorkRequest.Builder(BleScanWorker::class.java)
            //.setId(UUID.fromString(SCAN_ID))
            //.setInputData()
            .build()

        WorkManager.getInstance(application).enqueueUniqueWork(
            SCANNING_WORK, ExistingWorkPolicy.REPLACE, scanRequest)

        //WorkManager.getInstance(application).enqueue(scanRequest)*//*
    }*/

   /* @SuppressLint("MissingPermission")
    fun startScan() {

        bleScanCallback.apply {
            onBleScanCallbackListener = object : BleScanCallback.OnBleScanCallbackListener {
                override fun onDeviceListReady(string: String) {
                    Log.d(TAG, "onDeviceList ready and $string")

                    //_devicesSimple.value = devicesSimple
                }

                override fun onFailed() {
                    Log.d(TAG, "Got failes to scan use case")
                }
            }
        }

        bleScanner.startScan(null, settings, bleScanCallback)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        bleScanner.stopScan(bleScanCallback)
    }*/
}