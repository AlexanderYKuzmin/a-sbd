package com.example.a_sbd.di

import android.app.Application
import android.content.BroadcastReceiver
import com.example.a_sbd.data.database.ASBDDatabase
import com.example.a_sbd.data.database.ChatContactsDao
import com.example.a_sbd.data.repository.ASBDoRepositoryImpl
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.DeviceSimple
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun bindRepository(repositoryImpl: ASBDoRepositoryImpl): ASBDRepository

    companion object {

        @Provides
        fun provideChatContactsDao(application: Application): ChatContactsDao {
            return ASBDDatabase.getInstance(application).chatContactsDao()
        }
    }
}