package com.example.a_sbd.services

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.a_sbd.services.BleService.Companion.ACTION_SERVICE_CONNECTED
import com.example.a_sbd.ui.MainActivity
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class BleServiceConnection @Inject constructor(): ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val bleService = (service as BleService.LocalBinder).getService()
        bleService?.let { it ->
            // call functions on service to check connection and connect to devices
            if (!it.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth")
            }
            // perform device connection
            //it.connect(deviceAddress)
            it.updateBroadcast(ACTION_SERVICE_CONNECTED)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }
}
