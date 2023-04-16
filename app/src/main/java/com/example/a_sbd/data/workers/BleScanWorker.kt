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
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.ui.MainActivity.Companion.ACTION_DEVICES_FOUND
import com.example.a_sbd.ui.MainActivity.Companion.DEVICES_KEY
import com.example.a_sbd.ui.MainActivity.Companion.IS_SCAN_START
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
) : CoroutineWorker(appContext, workerParameters) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        Log.d(TAG, "do work scanner")

        val isScanStart = inputData.getBoolean(IS_SCAN_START, false)

        val intent = Intent(ACTION_DEVICES_FOUND)

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            SCAN_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        if (isScanStart) {
            val res = bleScanner.startScan(null, scanSettings, pendingIntent)
            Log.d(TAG, "scan result res = $res, start scanning")
        } else {
            Log.d(TAG, "scan worker stop scanning")
            bleScanner.stopScan(pendingIntent)
        }

        return Result.success()
    }

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