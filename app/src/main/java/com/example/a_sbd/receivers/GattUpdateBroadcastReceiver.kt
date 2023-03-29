package com.example.a_sbd.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.a_sbd.services.BleService.Companion.ACTION_GATT_CONNECTED
import javax.inject.Inject
/*

class GattUpdateBroadcastReceiver @Inject constructor() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                */
/*ACTION_GATT_CONNECTED -> {
                    isConnected = true
                    updateConnectionState("Connected")
                }
                ACTION_GATT_DISCONNECTED -> {
                    isConnected = false
                    updateConnectionState("Disconnected")
                }
                ACTION_GATT_SERVICES_DISCOVERED -> {
                    // Show all the supported services and characteristics on the user interface.
                    isServiceDiscovered = true
                    //displayGattServices(bluetoothService?.getSupportedGattServices())
                    characteristic = bleService?.getSupportedGattCharacteristic()

                    bleService?.setCharacteristicNotification(characteristic!!, true)
                    Thread.sleep(1000)

                    Log.d(TAG, "Receiver GATT_SERVICES_DISCOVERED. Write characteristic AT.")
                    bleService?.writeCharacteristic(characteristic!!, CHECK_MODEM)
                }
                ACTION_DATA_READ -> {
                    Log.d(TAG, "Receiver ACTION_DATA_READ. Display message.")
                    displayMessage(intent.getStringExtra(EXTRA_DATA))
                }
                ACTION_DATA_WRITTEN -> {
                    Log.d(TAG, "Receiver ACTION_DATA_WRITTEN. ")
                    displayMessage(intent.getStringExtra(EXTRA_DATA))
                }
                ACTION_DATA_AVAILABLE -> {
                    Log.d(TAG, "Receiver ACTION_DATA_AVAILABLE. Data have changed")
                    val text = intent.getStringExtra(EXTRA_DATA)

                    displayMessage(text)
                }
                ACTION_SIGNAL_LEVEL -> {
                    val level = intent.getIntExtra(SIGNAL_DATA, -1)
                    Log.d(TAG, "Receiver ACTION_SIGNAL_DATA. Signal level: $level")
                }
                ACTION_SERVICE_IDLE -> {
                    isServiceIdle = intent.getBooleanExtra(SERVICE_IDLE, false)
                }
                ACTION_MODEM_READY -> {
                    val isModemReady = intent.getBooleanExtra(MODEM_READY, false)
                    handleModemReady(isModemReady)
                }*//*

            }
        }
    }
}*/
