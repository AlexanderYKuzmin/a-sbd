package com.example.a_sbd.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import javax.inject.Inject

class ScanService @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

   /* fun initialize(): Boolean {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }*/


    inner class LocalBinder() : Binder() {
        fun getService() : ScanService {
            return this@ScanService
        }
    }
}