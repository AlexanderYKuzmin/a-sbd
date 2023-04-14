package com.example.a_sbd.services

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.a_sbd.ASBDApp
import com.example.a_sbd.data.bluetooth.BleScanCallback
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.ui.MainActivity.Companion.ACTION_DEVICES_FOUND
import com.example.a_sbd.ui.MainActivity.Companion.DEVICES_KEY
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
/*

class ScanService (
) : Service() {

    @Inject
    lateinit var bleAdapter: BluetoothAdapter

    @Inject
    lateinit var bleScanner: BluetoothLeScanner

    @Inject
    lateinit var scanSettings: ScanSettings

    @Inject
    lateinit var bleScanCallback: BleScanCallback

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopScan()
        return super.onUnbind(intent)
    }

    fun initialize(): Boolean {
        (application as ASBDApp).component.getServiceSubComponent().inject(this)
        return true
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        Log.d(TAG, "Service start scan")
        bleScanCallback.onBleScanCallbackListener = object : BleScanCallback.OnBleScanCallbackListener {
            override fun onDeviceListReady(devicesSimple: List<DeviceSimple>) {
                broadcastUpdate(ACTION_DEVICES_FOUND, devicesSimple as ArrayList<DeviceSimple>)
            }
        }
        runBlocking {
             launch {
                 bleScanner.startScan(null, scanSettings, bleScanCallback)
             }
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        bleScanner.stopScan(bleScanCallback)
    }

    private fun broadcastUpdate(action: String, devices: ArrayList<DeviceSimple>) {
        val intent = Intent(action)
        intent.putExtra(DEVICES_KEY, devices)
        sendBroadcast(intent)
    }


    inner class LocalBinder() : Binder() {
        fun getService() : ScanService {
            return this@ScanService
        }
    }
}*/
