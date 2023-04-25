package com.example.a_sbd.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class BleService @Inject constructor(
    private val bluetoothManager: BluetoothManager,
    private val bluetoothAdapter: BluetoothAdapter,
    private val bluetoothLeScanner: BluetoothLeScanner,
    private val settings: ScanSettings,
) : Service() {

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun initialize(): Boolean {
        return true
    }


    fun connect(address: String): Boolean {
        return false
    }

    fun updateBroadcast(action: String) {
        sendBroadcast(Intent(action))
    }

    inner class LocalBinder : Binder() {
        fun getService() : BleService {
            return this@BleService
        }
    }

    companion object {
        const val ACTION_SERVICE_CONNECTED = "service connected"

        const val ACTION_GATT_CONNECTED = "a_sbd.services.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "a_sbd.services.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "a_sbd.services.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "a_sbd.services.ACTION_DATA_AVAILABLE"
        const val ACTION_DATA_WRITTEN = "a_sbd.services.ACTION_DATA_WRITTEN"
        const val ACTION_DATA_READ = "a_sbd.services.ACTION_DATA_READ"
        const val ACTION_SIGNAL_LEVEL = "a_sbd.services.ACTION_SIGNAL_LEVEL"
        const val ACTION_SERVICE_IDLE = "a_sbd.services.ACTION_SERVICE_IDLE"
        const val ACTION_MODEM_READY = "a_sbd.services.ACTION_MODEM_READY"
        const val ACTION_MODEM_CIEV_DISABLE = "a_sbd.services.ACTION_MODEM_CIEV_DISABLE"
    }
}