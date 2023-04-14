package com.example.a_sbd.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject


@SuppressLint("MissingPermission")
class BleScanCallback @Inject constructor() : ScanCallback() {

    var onBleScanCallbackListener: OnBleScanCallbackListener? = null

    private val devices = mutableListOf<BluetoothDevice>()
    private val devicesSimple: List<DeviceSimple> by lazy {
        devices.map { bluetoothDevice -> DeviceSimple(bluetoothDevice.name, bluetoothDevice.address) }
    }

    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        onBleScanCallbackListener?.onDeviceListReady("READY")
        //Log.d(TAG, "Found device: ${result.device}")
        val indexQuery = devices.indexOfFirst { it.address == result.device.address }
        if (indexQuery != -1) {// device is already exist
            devices[indexQuery] = result.device
            //onBleScanCallbackListener?.onDeviceListReady(devicesSimple)

        } else {
            with(result.device) {
                Log.d(TAG, "Found BLE device! Name: ${name ?: "Unnamed"}, " +
                        "Address: $address")
            }
            devices.add(result.device)
            Log.d(TAG, "device added to devices: ${result.device.address}")

        }
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.d(TAG, "onScanFailed error code: $errorCode")
        onBleScanCallbackListener?.onFailed()
    }

    interface OnBleScanCallbackListener {
        //fun onDeviceListReady(devicesSimple: List<DeviceSimple>)
        fun onDeviceListReady(string: String)

        fun onFailed()
    }
}