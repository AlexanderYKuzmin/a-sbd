package com.example.a_sbd.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import javax.inject.Inject

class BleModemServiceWorker (
     private val appContext: Context,
     private val workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return Result.failure()
    }

    //@AssistedFactory
    //interface Factory : ChildWorkerFactory

    class Factory @Inject constructor() : ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return BleModemServiceWorker(appContext, workerParameters)
        }
    }

}