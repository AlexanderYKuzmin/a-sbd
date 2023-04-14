package com.example.a_sbd.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.util.Log
import com.example.a_sbd.data.workers.BleConnectionWorker
import com.example.a_sbd.ui.MainActivity.Companion.TAG

class BleConnectionGattCallback(
    //private val onGattCallbackListener: BleConnectionWorker.OnGattCallbackListener
) : BluetoothGattCallback() {

    var onGattCallbackListener: OnGattCallbackListener? = null

    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        Log.d(TAG, "onConnectionChanged")
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            // successfully connected to the GATT Server
            //connectionState = STATE_CONNECTED
            //broadcastUpdate(ACTION_GATT_CONNECTED)
            onGattCallbackListener?.onConnectionEstablished(true)
            Log.d(TAG, "Connected")
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // disconnected from the GATT Server
            //connectionState = STATE_DISCONNECTED
            //broadcastUpdate(ACTION_GATT_DISCONNECTED)
            onGattCallbackListener?.onConnectionEstablished(false)
        }
        gatt?.discoverServices()
    }
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
           // broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
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
        //broadcastUpdate(ACTION_DATA_AVAILABLE, value)
        //checkChangedCharacteristic(characteristic)
    }

    @Deprecated("Deprecated in Java")
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic!!.value)
        //checkChangedCharacteristic(characteristic!!)
    }

    /*private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }*/

    interface OnGattCallbackListener {
        fun onConnectionEstablished(isConnectionEstablished: Boolean)
    }
}