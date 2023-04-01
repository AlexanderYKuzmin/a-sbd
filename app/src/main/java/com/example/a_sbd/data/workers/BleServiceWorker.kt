package com.example.a_sbd.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class BleServiceWorker @Inject constructor(
    private val appContext: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

    /*@AssistedFactory
    interface Factory : ChildWorkerFactory*/

    class Factory @Inject constructor() : ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return BleServiceWorker(appContext, workerParameters)
        }
    }

}