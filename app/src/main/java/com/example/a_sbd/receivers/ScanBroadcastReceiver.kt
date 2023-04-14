package com.example.a_sbd.receivers

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.ui.MainActivity.Companion.ACTION_DEVICES_FOUND
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class ScanBroadcastReceiver @Inject constructor(
) : BroadcastReceiver() {

    var onScanResultListener: OnScanResultListener? = null

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent) {
        Log.d(TAG, "onReceive")
        //if (intent.hasExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)) {
        when (intent.action) {
              ACTION_DEVICES_FOUND -> {
                  val results = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                      intent.getParcelableArrayListExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT, ScanResult::class.java)
                  } else {
                      intent.getParcelableArrayListExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)
                  }

                  val devices =
                      results?.map { scanResult -> DeviceSimple(scanResult.device.name, scanResult.device.address) }

                  onScanResultListener?.setScanResult(devices)
              }
        }

       // }
    }

    interface OnScanResultListener {
        fun setScanResult(devices: List<DeviceSimple>?)
    }
}