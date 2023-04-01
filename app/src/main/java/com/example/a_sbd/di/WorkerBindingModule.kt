package com.example.a_sbd.di

import com.example.a_sbd.data.workers.BleServiceWorker
import com.example.a_sbd.data.workers.ChildWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(BleServiceWorker::class)
    fun bindBleServiceWorker(factory: BleServiceWorker.Factory): ChildWorkerFactory
}