package com.example.a_sbd.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.a_sbd.services.BleService.Companion.ACTION_SERVICE_CONNECTED
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject
/*

class ScanServiceConnection @Inject constructor(
): ServiceConnection {

    var onScanServiceConnectionListener: OnScanServiceConnectionListener? = null

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val scanService = (service as ScanService.LocalBinder).getService()
        scanService?.let { it ->
            // call functions on service to check connection and connect to devices
            if (!it.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth")
            }

            onScanServiceConnectionListener?.setServiceToViewModel(scanService)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }

    interface OnScanServiceConnectionListener {
        fun setServiceToViewModel(service: ScanService?)
    }
}
*/
