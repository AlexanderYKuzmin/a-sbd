package com.example.a_sbd.di

import android.app.Application
import android.bluetooth.BluetoothManager
import com.example.a_sbd.data.workers.AppWorkerFactory
import com.example.a_sbd.ui.MainActivity
import com.example.a_sbd.ui.chats.ChatContactsFragment
import com.example.a_sbd.ui.chats.ChatContactsViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class, ViewModelModule::class, BluetoothModule::class,
     WorkerBindingModule::class])
interface ASBDComponent {

    fun inject(mainActivity: MainActivity)

    //fun appFactory(): AppWorkerFactory

    //fun inject(chatContactsViewModel: ChatContactsViewModel)

    fun getChatContactsSubComponent(): ChatSubComponent


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ASBDComponent
    }
}