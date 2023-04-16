package com.example.a_sbd.di

import com.example.a_sbd.data.workers.BleConnectionWorker
import com.example.a_sbd.data.workers.BleScanWorker
import com.example.a_sbd.data.workers.BleModemServiceWorker
import com.example.a_sbd.data.workers.ChildWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(BleModemServiceWorker::class)
    fun bindBleModemServiceWorker(factory: BleModemServiceWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(BleScanWorker::class)
    fun bindBleScanWorker(factory: BleScanWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(BleConnectionWorker::class)
    fun bindBleConnectionWorker(factory: BleConnectionWorker.Factory): ChildWorkerFactory
}
