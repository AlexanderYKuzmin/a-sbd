package com.example.a_sbd.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.a_sbd.data.bluetooth.BleConnectionGattCallback
import com.example.a_sbd.data.commands.*
import com.example.a_sbd.data.mapper.ModemResponseMapper
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.ui.MainActivity.Companion.TAG

class BleService : LifecycleService() {

    private val binder = LocalBinder()

    lateinit var bleConnectionGattCallback: BleConnectionGattCallback

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val mapper: ModemResponseMapper = ModemResponseMapper()
    private val jsonConverter = JsonConverter()

    private var bluetoothGatt: BluetoothGatt? = null
    private var characteristic: BluetoothGattCharacteristic? = null

    private var _isServiceIdle = false

    private var messageIdToWrite = -1L

    private val _testLiveData = MutableLiveData<Unit>()
        // do i need live data?

    private var signalQuality: Int = 0

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    fun initialize(adapter: BluetoothAdapter): Boolean {
        bluetoothAdapter = adapter
        bleConnectionGattCallback = BleConnectionGattCallback().apply {
            onGattCallbackListener = bleListener
        }

        return true
    }


    @SuppressLint("MissingPermission")
    fun connect(address: String): Boolean {
        return try {
            val device = bluetoothAdapter.getRemoteDevice(address)
            bluetoothGatt = device.connectGatt(this, false, bleConnectionGattCallback)
            Thread.sleep(200)
            true
        } catch (exception: IllegalArgumentException) {
            Log.w(TAG, "Device not found with provided address.")
            false
        }
    }

    fun disconnect() {
        bluetoothGatt = null
    }

    fun checkSignalLevel() {
        writeCharacteristic(GET_SIGNAL_QUALITY_LEVEL, null)
    }

    fun openSession() {
        writeCharacteristic(INITIATE_SBD_SESSION, null)
    }

    fun writeToBuffer(text: String, id: Long) {
        Log.d(TAG, "BleService. Writing to buffer.")
        messageIdToWrite = id
        writeCharacteristic(WRITE_TEXT_COMMAND, toConsistentText(text))
    }

    fun readFromBuffer() {
        // todo cycling maybe
    }

    fun setSbdRingState() {

    }

    fun resetMessageId() {
        messageIdToWrite = -1L
    }

    @SuppressLint("MissingPermission")
    private fun writeCharacteristic(modemCommand: String, parameters: String?) {
        _isServiceIdle = false
        val value = if (parameters != null) {
            modemCommand + COMMAND_DELIMITER + parameters + END_OF_COMMAND
        } else {
            modemCommand + END_OF_COMMAND
        }.toByteArray()

        Log.d(TAG, "Write characteristic: $value")
        bluetoothGatt?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // 33
                    it.writeCharacteristic(characteristic!!, value , BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)

            } else {
                    characteristic?.value = value
                    it.writeCharacteristic(characteristic!!)
            }
            /*while (!_isTaskDone) {

            }*/
            Thread.sleep(200)
        }
    }

    /*private fun updateBroadcast(action: String, isSuccess: Boolean) {
        val intent = Intent(action)
        intent.putExtra(IS_CONNECTED, isSuccess)
        sendBroadcast(intent)
    }

    private fun updateBroadcast(action: String, responseValue: Int) {
        val intent = Intent(action)
        intent.putExtra(action, responseValue)
        sendBroadcast(intent)
    }*/

    private fun updateBroadcast(action: String, obj: Any) {
        val intent = Intent(action)
        when (obj) {
            is Boolean -> intent.putExtra(action, obj)
            is String -> intent.putExtra(action, obj)
            is Int -> intent.putExtra(action, obj)
            is Long -> intent.putExtra(action, obj)
            else -> throw RuntimeException("Invalid type of action")
        }
        sendBroadcast(intent)
    }

    /*private fun updateBroadcast(action: String, map: Map<String, Int>) {
        val intent = Intent(action)
        intent.putExtra(action, map as HashMap)
        sendBroadcast(intent)
    }*/

    private val bleListener: BleConnectionGattCallback.OnGattCallbackListener = object : BleConnectionGattCallback.OnGattCallbackListener {
        @SuppressLint("MissingPermission")
        override fun onConnectionEstablished(
            isConnectionEstablished: Boolean,
            characteristic: BluetoothGattCharacteristic?
        ) {
            if (isConnectionEstablished) {
                Log.d(TAG, "On connection established")
                this@BleService.characteristic = characteristic
                bluetoothGatt?.setCharacteristicNotification(this@BleService.characteristic, true)
                updateBroadcast(ACTION_GATT_CONNECTED, isConnectionEstablished)
            } else {
                Log.d(TAG, "Gatt disconnected")
            }

        }

        override fun onServicesDisCovered(characteristic: BluetoothGattCharacteristic) {
            TODO("Not yet implemented")
        }

        override fun onCharacteristicChanged(response: String) {
            Log.d(TAG, "onCharacteristic changed: $response")

            val value = response
            when  {
                value.contains("CSQ") -> {
                    val signal = mapper.parseSignalQuality(value)
                    updateBroadcast(ACTION_SIGNAL_LEVEL, signal)
                }
                value.contains("SBDIX") -> {
                    val sessionData = mapper.parseSBDIXResponse(value) as HashMap
                    if (messageIdToWrite != -1L) sessionData[MESSAGE_ID] = messageIdToWrite.toInt() // todo change map to data class may be
                    updateBroadcast(ACTION_DATA_AVAILABLE, jsonConverter.toJson(sessionData))
                }
                value.contains("SBDWT") -> {
                    Log.d(TAG, "Response contains SBDWT. Sending action data.")
                    updateBroadcast(ACTION_DATA_WRITTEN, messageIdToWrite)
                    checkSignalLevel()
                }
            }

            _isServiceIdle = true  //
        }
    }

    private fun toConsistentText(text: String): String {
        return if (text.length > 117) text.substring(0, 118) else text
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

        const val IS_CONNECTED = "is_gatt_connected"
        const val SIGNAL_QUALITY = "signal_quality"

        //SESSIONS parameters
        const val MO_STATUS = "moStatus"
        const val MT_STATUS = "mtStatus"
        const val MO_MSN = "moMsn"
        const val MT_MSN = "mtMsn"
        const val QUEUE_LENGTH = "queueLength"
        const val MESSAGE_ID = "message_id"

    }
}