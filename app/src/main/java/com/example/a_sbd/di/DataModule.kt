package com.example.a_sbd.di

import android.app.Application
import androidx.work.WorkManager
import com.example.a_sbd.data.repository.ASBDoRepositoryImpl
import com.example.a_sbd.data.workers.BleServiceWorker
import com.example.a_sbd.domain.ASBDRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun bindRepository(repositoryImpl: ASBDoRepositoryImpl): ASBDRepository

   /* companion object {

        @Provides
        @Singleton
        fun provideBleServiceWorker(application: Application): BleServiceWorker {
            return WorkManager.getInstance(application)
        }
    }*/
}