package com.example.a_sbd.domain.usecases

import android.app.Application
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.a_sbd.R
import com.example.a_sbd.data.workers.BleConnectionWorker
import com.example.a_sbd.data.workers.BleScanWorker
import com.example.a_sbd.ui.MainActivity
import com.example.a_sbd.ui.MainActivity.Companion.CONNECTING_WORK
import com.example.a_sbd.ui.MainActivityViewModel.Companion.CONNECTION_TAG
import javax.inject.Inject

class SetBleConnectionUseCase @Inject constructor(
    private val application: Application
) {
    operator fun invoke(address: String) {
        Log.d(MainActivity.TAG, "Invoke SetBleConnection")

        val inputData = Data.Builder()
            .putString(DEVICE_ADDRESS, address)
            .build()

        val connectRequest = OneTimeWorkRequest.Builder(BleConnectionWorker::class.java)
            .addTag(CONNECTION_TAG)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueueUniqueWork(
            CONNECTING_WORK, ExistingWorkPolicy.REPLACE, connectRequest)
    }

    companion object {
        const val DEVICE_ADDRESS = "address"
    }
}