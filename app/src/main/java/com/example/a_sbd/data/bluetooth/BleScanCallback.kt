package com.example.a_sbd.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import com.example.a_sbd.ui.MainActivity.Companion.SCAN_TAG
import com.example.a_sbd.ui.MainActivityViewModel
import javax.inject.Inject

class BleScanCallback @Inject constructor(
    val viewModel: MainActivityViewModel
): ScanCallback() {
    private val devices = mutableListOf<BluetoothDevice>()

    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        Log.d(SCAN_TAG, "Found device: ${result.device}")
        val indexQuery = devices.indexOfFirst { it.address == result.device.address }
        if (indexQuery != -1) {// device is already exist
            devices[indexQuery] = result.device
            //viewModel.stopScan()
        } else {
            with(result.device) {
                Log.d(SCAN_TAG, "Found BLE device! Name: ${name ?: "Unnamed"}, " +
                        "Address: $address")
            }
            devices.add(result.device)
            Log.d(SCAN_TAG, "device added to devices: ${result.device.address}")
        }
        //if (!mutableListDevices.isNullOrEmpty()) _devices.value = mutableListDevices

    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.d(SCAN_TAG, "onScanFailed error code: $errorCode")
    }
}