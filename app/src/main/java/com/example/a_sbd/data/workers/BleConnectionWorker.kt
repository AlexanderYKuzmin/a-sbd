package com.example.a_sbd.data.workers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.a_sbd.data.bluetooth.BleConnectionGattCallback
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase.Companion.DEVICE_ADDRESS
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class BleConnectionWorker(
    private val appContext: Context,
    private val workerParameters: WorkerParameters,
    //private val bleAdapter: BluetoothAdapter,
) : CoroutineWorker(appContext, workerParameters){

    private var bluetoothGatt: BluetoothGatt? = null

    private var _isConnectionEstablished = false

    private val bluetoothGattCallback = BleConnectionGattCallback().apply {
        onGattCallbackListener = object : BleConnectionGattCallback.OnGattCallbackListener {
            override fun onConnectionEstablished(isConnectionEstablished: Boolean) {
                _isConnectionEstablished = isConnectionEstablished
            }
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        Log.d(TAG, "Do work BleConnection")
        val address = inputData.getString(DEVICE_ADDRESS)
        if (address != null) {
            //setConnection(address)

            val boolean = false//setConnection(address)
            Log.d(TAG, "Connection established: $boolean")
            while (true) {
                if (_isConnectionEstablished) {
                    Log.d(TAG, "bluetooth is connected")
                    return Result.success()
                }
                delay(500)
                Log.d(TAG, "awaiting connection...")
            }
        }

        //val modemCommand = inputData.getString()

        //val device = bleAdapter.getRemoteDevice(address)
        return Result.failure()

    }


    /*@SuppressLint("MissingPermission")
    private fun setConnection(address: String): Boolean {
        Log.d(TAG, "Setting connection...")
        //bluetoothGatt = bleAdapter.getRemoteDevice(address).connectGatt(appContext, false, bluetoothGattCallback)
        bleAdapter.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                bluetoothGatt = device.connectGatt(appContext, false, bluetoothGattCallback)
                return true
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address.")
                return false
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return false
        }

    }*/

    class Factory @Inject constructor(
        //private val bleAdapter: BluetoothAdapter
    ): ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return BleConnectionWorker(
                appContext,
                workerParameters,
                )
        }
    }
}