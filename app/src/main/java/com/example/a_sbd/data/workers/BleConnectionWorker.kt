package com.example.a_sbd.data.workers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import android.util.LogPrinter
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.a_sbd.data.bluetooth.BleConnectionGattCallback
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.data.workers.commands.BLE_CONNECTION_ESTABLISHED
import com.example.a_sbd.data.workers.commands.BLE_CONNECTION_FAILED
import com.example.a_sbd.data.workers.commands.SET_BLE_CONNECTION
import com.example.a_sbd.domain.model.WorkBleConnectionResponse
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase.Companion.DEVICE_ADDRESS
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import com.example.a_sbd.ui.MainActivityViewModel.Companion.COMMAND
import com.example.a_sbd.ui.MainActivityViewModel.Companion.REPORT
import kotlinx.coroutines.delay
import javax.inject.Inject

class BleConnectionWorker(
    private val appContext: Context,
    private val workerParameters: WorkerParameters,
    private val bleAdapter: BluetoothAdapter,
    private val jsonConverter: JsonConverter
) : CoroutineWorker(appContext, workerParameters){

    private var bluetoothGatt: BluetoothGatt? = null

    private var characteristic: BluetoothGattCharacteristic? = null

    private var _isWorkDone = false

    private var response: WorkBleConnectionResponse? = null

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        Log.d(TAG, "Do work BleConnection")
        _isWorkDone = false
        val command = inputData.getInt(COMMAND, -1)

        val report = Data.Builder()

        when (command) {
            SET_BLE_CONNECTION -> {
                setConnection()
            }
            // todo Check modem level
        }

        while (true) {
            if (_isWorkDone) {
                Log.d(TAG, "The work is completed")

                return Result.success(report.putString(REPORT, jsonConverter.toJson(report)).build())
            }
            delay(500)
            Log.d(TAG, "awaiting connection...")
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
                bluetoothGatt = device.connectGatt(appContext, false, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address.")
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")

        }

    }

    private val bluetoothGattCallback = BleConnectionGattCallback().apply {
        onGattCallbackListener = object : BleConnectionGattCallback.OnGattCallbackListener {
            override fun onConnectionEstablished(isConnectionEstablished: Boolean, chara: BluetoothGattCharacteristic?) {

                response = WorkBleConnectionResponse(
                    SET_BLE_CONNECTION,
                    null,
                    if (isConnectionEstablished && chara != null) BLE_CONNECTION_ESTABLISHED
                    else BLE_CONNECTION_FAILED,
                    null
                )
                characteristic = chara
                _isWorkDone = true
            }

            override fun onServicesDisCovered(characteristic: BluetoothGattCharacteristic) {
                TODO("Not yet implemented")
            }
        }
    }

    class Factory @Inject constructor(
        private val bleAdapter: BluetoothAdapter,
        private val jsonConverter: JsonConverter
    ): ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return BleConnectionWorker(
                appContext,
                workerParameters,
                bleAdapter,
                jsonConverter
                )
        }
    }
}