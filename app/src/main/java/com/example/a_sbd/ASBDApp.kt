package com.example.a_sbd

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.a_sbd.data.workers.AppWorkerFactory
import com.example.a_sbd.di.ASBDComponent
import com.example.a_sbd.di.DaggerASBDComponent
import javax.inject.Inject

class ASBDApp : Application() {

    lateinit var component: ASBDComponent

    @Inject
    lateinit var factory: AppWorkerFactory

    override fun onCreate() {
        component = DaggerASBDComponent.factory().create(this)
        super.onCreate()

        WorkManager.initialize(
            this, Configuration.Builder().setWorkerFactory(factory).build()
        )
    }
}