package com.example.a_sbd.domain.usecases

import android.app.Application
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.a_sbd.data.workers.BleConnectionWorker
import com.example.a_sbd.data.workers.commands.REMOVE_BLE_CONNECTION
import com.example.a_sbd.data.workers.commands.SET_BLE_CONNECTION
import com.example.a_sbd.ui.MainActivity
import com.example.a_sbd.ui.MainActivityViewModel.Companion.CONNECTION_WORK
import com.example.a_sbd.ui.MainActivityViewModel.Companion.WORKER_COMMAND
import javax.inject.Inject

class SetBleConnectionUseCase @Inject constructor(
    private val application: Application
) {
    operator fun invoke(address: String?) {
        Log.d(MainActivity.TAG, "Invoke SetBleConnection")

        val inputData = if (address.isNullOrEmpty()) {
            Data.Builder()
                .putInt(WORKER_COMMAND, REMOVE_BLE_CONNECTION)
                .build()
        } else {
            Data.Builder()
                .putInt(WORKER_COMMAND, SET_BLE_CONNECTION)
                .putString(DEVICE_ADDRESS, address)
                .build()
        }

        val connectRequest = OneTimeWorkRequest.Builder(BleConnectionWorker::class.java)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueueUniqueWork(
            CONNECTION_WORK, ExistingWorkPolicy.REPLACE, connectRequest
        )
    }

    companion object {
        const val DEVICE_ADDRESS = "address"
    }
}