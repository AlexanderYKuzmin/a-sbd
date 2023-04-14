package com.example.a_sbd.di

import android.content.BroadcastReceiver
import com.example.a_sbd.data.repository.ASBDoRepositoryImpl
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.receivers.ScanBroadcastReceiver
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun bindRepository(repositoryImpl: ASBDoRepositoryImpl): ASBDRepository

    /*@Binds
    fun bindOnScanResultListener(): ScanBroadcastReceiver.OnScanResultListener*/

    companion object {

        /*@Provides
        @Singleton
        fun provideBleServiceWorker(application: Application): BleServiceWorker {
            return WorkManager.getInstance(application)
        }*/

        /*@Provides
        @Singleton
        fun provideOnScanResultListener(): ScanBroadcastReceiver.OnScanResultListener {
            return object : ScanBroadcastReceiver.OnScanResultListener {
                override fun getScanResult(devices: List<DeviceSimple>?) {

                }
            }
        }*/
    }
}