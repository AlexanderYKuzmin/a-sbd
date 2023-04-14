package com.example.a_sbd.data.workers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.a_sbd.data.bluetooth.BleScanCallback
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.receivers.ScanBroadcastReceiver
import com.example.a_sbd.ui.MainActivity.Companion.ACTION_DEVICES_FOUND
import com.example.a_sbd.ui.MainActivity.Companion.DEVICES_KEY
import com.example.a_sbd.ui.MainActivity.Companion.SCAN_INTENT_REQUEST_CODE
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BleScanWorker (
     private val appContext: Context,
     private val workerParameters: WorkerParameters,
     private val bleScanner: BluetoothLeScanner,
     private val scanSettings: ScanSettings,
     //private val jsonConverter: JsonConverter
) : CoroutineWorker(appContext, workerParameters) {

    private var devicesSimple: List<DeviceSimple>? = null

    /*private val bleScanCallback = BleScanCallback(object : BleScanCallback.OnBleScanCallbackListener {
        override fun onDeviceListReady(devicesSimple: List<DeviceSimple>) {
            this@BleScanWorker.devicesSimple = devicesSimple
            Log.d(TAG, "devicesSimple in listener: ${devicesSimple.size}")
        }
    })*/

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        Log.d(TAG, "do work scanner")

        /*bleScanner.startScan(null, scanSettings, bleScanCallback)
        Log.d(TAG, "bleScanner: $bleScanner")*/
        val intent = Intent(
            appContext,
            ScanBroadcastReceiver::class.java
        ).setAction(ACTION_DEVICES_FOUND)

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            SCAN_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        val res = bleScanner.startScan(null, scanSettings, pendingIntent)
        /*while (true)
        {
            Log.d(TAG, "in the cycling. deviceSimple: ${devicesSimple?.size}")
            if (devicesSimple != null) {
                Log.d(TAG, "in the cycling. deviceSimple: ${devicesSimple?.size}")
                runBlocking {
                    launch { bleScanner.stopScan(bleScanCallback) }
                }

                val devicesJson = jsonConverter.toJson(devicesSimple!!)
                val outputData = workDataOf(DEVICES_KEY to devicesJson)
                return Result.success(outputData)
            }
            delay(5000)
        }*/
            /*val devicesJson = Gson().toJson(listOf(
            DeviceSimple("name1", "44:55:66"),
            DeviceSimple("BLE05", "78:C5:E5:72:CD:84")
            )
        )*/
        delay(10000)
        Log.d(TAG, "scan result res = $res")
        bleScanner.stopScan(pendingIntent)
        return Result.success()
    }

    //@AssistedFactory
    class Factory @Inject constructor(
        private val bleScanner: BluetoothLeScanner,
        private val scanSettings: ScanSettings,
    ): ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return BleScanWorker(
                appContext,
                workerParameters,
                bleScanner,
                scanSettings,
            )
        }
    }
}