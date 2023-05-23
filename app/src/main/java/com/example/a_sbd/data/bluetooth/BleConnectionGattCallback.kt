package com.example.a_sbd.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.util.Log
//import com.example.a_sbd.data.workers.BleConnectionWorker
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import java.util.*
import kotlin.concurrent.thread

class BleConnectionGattCallback(
    //private val onGattCallbackListener: OnGattCallbackListener
) : BluetoothGattCallback() {

    var onGattCallbackListener: OnGattCallbackListener? = null

    private val responseBuilder = StringBuilder()

    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        Log.d(TAG, "onConnectionChanged")
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            // successfully connected to the GATT Server
            //connectionState = STATE_CONNECTED
            //broadcastUpdate(ACTION_GATT_CONNECTED)
            //onGattCallbackListener?.onConnectionEstablished(true)
            Log.d(TAG, "Connected")
            gatt?.requestMtu(128)
            Thread.sleep(1000)
            gatt?.discoverServices()
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // disconnected from the GATT Server
            //connectionState = STATE_DISCONNECTED
            //broadcastUpdate(ACTION_GATT_DISCONNECTED)
            onGattCallbackListener?.onConnectionEstablished(false, null)
        }

    }
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        Log.d("Gatt Callback", "On services discovered")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d("Gatt Callback", "status success")
            val characteristic = gatt?.getService(UNKNOWN_SERVICE)?.getCharacteristic(UNKNOWN_CHARACTERISTIC)
            onGattCallbackListener?.onConnectionEstablished(true, characteristic)
            Log.d(TAG, "Gatt Callback listener: $onGattCallbackListener")
        } else {
            //Log.w(TAG, "onServicesDiscovered received: $status")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //broadcastUpdate(ACTION_DATA_READ, characteristic, INCOMING)
        }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val charaValue = String(characteristic.value)
            Log.d("Gatt Callback", "Write characteristic")
            /*if (charaValue.startsWith("AT+SBDWT")) {
                val charaValueSubstring = charaValue.substring(9)
                broadcastUpdate(ACTION_DATA_WRITTEN, charaValueSubstring, OUTGOING)
            }*/
            /*if (!isServiceOn) {
                //broadcastUpdate(ACTION_DATA_WRITTEN, characteristic, OUTGOING)
                broadcastReceiverStringBuilder.append(charaValue)
                if (charaValue.endsWith("\r\n")) {
                    broadcastUpdate(ACTION_DATA_WRITTEN, broadcastReceiverStringBuilder.toString(), OUTGOING)
                    broadcastReceiverStringBuilder.clear()
                }
            }*/
        }
        //isGattProcedureExecuted = true
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ) {
        if (isResponse(String(characteristic.value).trim())) {
            val finalResponse = responseBuilder.toString()
            responseBuilder.clear()
            onGattCallbackListener?.onCharacteristicChanged(finalResponse)
        } else {
            Log.d(TAG, "Characteristic value is not response or it has a one's part: ${String(characteristic.value)}")
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic
    ) {
        if (isResponse(String(characteristic.value).trim())) {
            val finalResponse = responseBuilder.toString()
            responseBuilder.clear()
            onGattCallbackListener?.onCharacteristicChanged(finalResponse)
        } else {
            Log.d(TAG, "Characteristic value is not a response or it has one's part: ${String(characteristic.value)}")
        }
    }

    private fun isResponse(value: String): Boolean {
        /*if (value.startsWith("+") && value.endsWith("OK") && responseBuilder.isEmpty()) {
            responseBuilder.append(value)
            return true
        } else if (value.startsWith("+") && responseBuilder.isEmpty()) {
            responseBuilder.append(value)
        } else if (value.endsWith("OK") && responseBuilder.isNotEmpty()) {
            responseBuilder.append(value)
            return true
        } else if (responseBuilder.isNotEmpty()){
            Log.d(TAG, "is Response condition when builder NOT EMPTY: $value")
            responseBuilder.append(value)
        }*/
        responseBuilder.append(value)
        return (value.startsWith("SBDRING") || value.endsWith("OK"))
        //return false
    }

    interface OnGattCallbackListener {
        fun onConnectionEstablished(isConnectionEstablished: Boolean,  characteristic: BluetoothGattCharacteristic?)

        fun onServicesDiscovered(characteristic: BluetoothGattCharacteristic)

        fun onCharacteristicChanged(response: String)
    }

    companion object {
        private val UNKNOWN_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
        private val UNKNOWN_CHARACTERISTIC = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    }
}