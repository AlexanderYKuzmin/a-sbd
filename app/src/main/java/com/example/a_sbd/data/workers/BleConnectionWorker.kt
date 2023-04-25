package com.example.a_sbd.data.workers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.a_sbd.data.bluetooth.BleConnectionGattCallback
import com.example.a_sbd.data.mapper.CharacteristicValueMapper
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.data.workers.commands.*
import com.example.a_sbd.domain.model.WorkBleConnectionResponse
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase.Companion.DEVICE_ADDRESS
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import com.example.a_sbd.ui.MainActivityViewModel.Companion.WORKER_COMMAND
import com.example.a_sbd.ui.MainActivityViewModel.Companion.REPORT
import kotlinx.coroutines.delay
import javax.inject.Inject

/*class BleConnectionWorker(
    private val appContext: Context,
    private val workerParameters: WorkerParameters,
    private val bleAdapter: BluetoothAdapter,
    private val jsonConverter: JsonConverter,
    private val characteristicValueMapper: CharacteristicValueMapper
) : CoroutineWorker(appContext, workerParameters){

    //private var bluetoothGatt: BluetoothGatt? = null

    private var characteristic: BluetoothGattCharacteristic? = null

    private var _isWorkDone = false

    private var characteristicResponseBuilder = StringBuilder()
    private var _response: WorkBleConnectionResponse? = null
    val response: WorkBleConnectionResponse?
        get() = _response

    private var workerCommand: Int = -1

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        //bluetoothGatt = BleGatt.bluetoothGatt
        characteristic = BleGatt.characteristic
        Log.d(TAG, "Do work BleConnection")
        _isWorkDone = false
        workerCommand = inputData.getInt(WORKER_COMMAND, -1)
        Log.d(TAG, "worker command: $workerCommand")

        _response = WorkBleConnectionResponse(workerCommand, null, -1, null)

        when (workerCommand) {
            SET_BLE_CONNECTION -> {
                Log.d(TAG, "Set BLE connection by worker command")
                setConnection()
            }
            REMOVE_BLE_CONNECTION -> {
                removeConnection()
            }

            MODEM_SIGNAL_QUALITY -> {
                Log.d(TAG, "BleConnectionWorker check signal: $workerCommand")
                checkSignalQuality()
            }
        }

        var counter = 0
        while (counter < 30) {
            if (_isWorkDone) {
                Log.d(TAG, "The work is completed")

                val report = Data.Builder()
                    .putString(REPORT, jsonConverter.toJson(_response))
                    .build()

                return Result.success(report)
            }
            delay(1000)
            Log.d(TAG, "Awaiting finishing work: $workerCommand")
            counter++
        }

        //val modemCommand = inputData.getString()

        //val device = bleAdapter.getRemoteDevice(address)
        return Result.failure()

    }


    @SuppressLint("MissingPermission")
    private fun setConnection() {
        Log.d(TAG, "Setting connection...")
        val address = inputData.getString(DEVICE_ADDRESS)
        //bluetoothGatt = bleAdapter.getRemoteDevice(address).connectGatt(appContext, false, bluetoothGattCallback)
        bleAdapter.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                BleGatt.bluetoothGatt = device.connectGatt(appContext, false, bluetoothGattCallback)
                Log.d(TAG, "Gatt = ${BleGatt.bluetoothGatt}")
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address.")
            }
        }

        //BleGatt.bluetoothGatt = bluetoothGatt
    }

    @SuppressLint("MissingPermission")
    private fun removeConnection() {
        Log.d(TAG, "Deleting connection...")
        if (BleGatt.bluetoothGatt != null) {
            BleGatt.bluetoothGatt!!.close()
            BleGatt.bluetoothGatt = null
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkSignalQuality() {
        Log.d(TAG, "Check signal quality starts. BleGatt = ${BleGatt.bluetoothGatt}")
        BleGatt.bluetoothGatt?.setCharacteristicNotification(characteristic, true)
        writeCharacteristic(GET_SIGNAL_QUALITY_LEVEL)

    }


    @SuppressLint("MissingPermission")
    private fun readCharacteristic() {
        BleGatt.bluetoothGatt?.let { gatt ->
            gatt.readCharacteristic(characteristic)
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
            return
        }
    }

    @SuppressLint("MissingPermission")
    private fun writeCharacteristic(text: String) {
        Log.d(TAG, "Write characteristic")
        Log.d(TAG, "Bluetooth Gatt: ${BleGatt.bluetoothGatt}")
        BleGatt.bluetoothGatt?.let { gatt ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // 33
                    gatt.writeCharacteristic(characteristic!!, text.toByteArray(), BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
            } else {
                    gatt.writeCharacteristic(characteristic)
            }
        }
    }*/

    /*@SuppressLint("MissingPermission")
    private fun writeCharacteristic(text: String) {
        //if (text.startsWith("AT+SBDWT")) isServiceOn = false

        val separatedByteArray = characteristicValueMapper.getSeparatedByteArrays(text.toByteArray())
        //if (separatedByteArray.size > 1) setCharacteristicNotification(characteristic, false)

        bluetoothGatt?.let { gatt ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // 33
                for (i in separatedByteArray.indices) {
                    //if (i == separatedByteArray.size - 1) setCharacteristicNotification(characteristic, true)
                    gatt.writeCharacteristic(characteristic!!, separatedByteArray[i], BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                }
            } else {
                for (i in separatedByteArray.indices) {
                    //if (i == separatedByteArray.size - 1) setCharacteristicNotification(characteristic, true)
                    characteristic!!.value = separatedByteArray[i]
                    gatt.writeCharacteristic(characteristic)
                    Log.d(TAG, "GATT has written value to characteristic: ${String(characteristic!!.value)}, length: ${String(characteristic!!.value).length}")
                    //Log.d(TAG, "Waiting a second. Part: $i, separatedByteArray size: ${separatedByteArray.indices}")
                    Thread.sleep(125)
                }
            }
        }
    }

    private fun isCharacteristicDataResponse() : Boolean { // Check if the response from callback is the response from modem
      //  val text = String(characteristic!!.value)
    //    if (characteristicResponseBuilder.isEmpty() && text.contains()) //проверить на начало модемной команды

        //  так не пойдет. иожет прийти пол характеристики
        return false
    }

    private val bluetoothGattCallback = BleConnectionGattCallback().apply {
        onGattCallbackListener = object : BleConnectionGattCallback.OnGattCallbackListener {
            override fun onConnectionEstablished(isConnectionEstablished: Boolean, chara: BluetoothGattCharacteristic?) {

                _response = response?.copy(
                    responseWorker = if (isConnectionEstablished && chara != null) BLE_CONNECTION_ESTABLISHED
                    else BLE_CONNECTION_FAILED_OR_REMOVED,
                )
                BleGatt.characteristic = chara

                Log.d(TAG, "On connection established. Response: ${response.toString()}")
                _isWorkDone = true
            }

            override fun onServicesDisCovered(characteristic: BluetoothGattCharacteristic) {
                TODO("Not yet implemented")
            }

            override fun onCharacteristicChanged(chara: BluetoothGattCharacteristic) {
                Log.d(TAG, "Characteristic changed")
                characteristic = chara
                when (workerCommand) {
                    MODEM_SIGNAL_QUALITY -> {
                        Log.d(TAG, "OnCharacteristic changed Worker for Modem command: $workerCommand")
                        Log.d(TAG, "Characteristic value: ${String(chara.value)}")
                        //TODO Check characteristic of modem signal quality
                        _response = response?.copy(
                            responseModem = String(chara.value)
                        )
                        _isWorkDone = true
                    }
                }

            }
        }
    }

    private fun collectStringBuilder() {

    }

    companion object {
        //private var bluetoothGatt: BluetoothGatt? = null
    }

    class Factory @Inject constructor(
        private val bleAdapter: BluetoothAdapter,
        private val jsonConverter: JsonConverter,
        private val characteristicValueMapper: CharacteristicValueMapper
    ): ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return BleConnectionWorker(
                appContext,
                workerParameters,
                bleAdapter,
                jsonConverter,
                characteristicValueMapper
                )
        }
    }
}*/