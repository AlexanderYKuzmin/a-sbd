package com.example.a_sbd.domain.usecases

import android.app.Application
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.a_sbd.data.commands.GET_SIGNAL_QUALITY_LEVEL
import com.example.a_sbd.data.commands.MODEM_SIGNAL_QUALITY
import com.example.a_sbd.ui.MainActivityViewModel.Companion.CHECK_SIGNAL_TAG
import com.example.a_sbd.ui.MainActivityViewModel.Companion.CHECK_SIGNAL_WORK
import com.example.a_sbd.ui.MainActivityViewModel.Companion.MODEM_COMMAND
import com.example.a_sbd.ui.MainActivityViewModel.Companion.WORKER_COMMAND
import javax.inject.Inject

class CheckSignalLevelUseCase @Inject constructor(
    private val application: Application
){

    /*operator fun invoke() {

        val data = Data.Builder()
            .putInt(WORKER_COMMAND, MODEM_SIGNAL_QUALITY)
            .build()


        val checkSignalRequest = OneTimeWorkRequest.Builder(BleConnectionWorker::class.java)
            .addTag(CHECK_SIGNAL_TAG)
            .setInputData(data)
            .build()

        WorkManager.getInstance(application)
            .enqueueUniqueWork(CHECK_SIGNAL_WORK, ExistingWorkPolicy.REPLACE, checkSignalRequest)
    }*/
}