package com.example.a_sbd.di

import androidx.lifecycle.ViewModel
import com.example.a_sbd.ui.MainActivityViewModel
import com.example.a_sbd.ui.chats.ChatContactsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Binds
    fun bindMainActivityViewModel(viewModel:MainActivityViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ChatContactsViewModel::class)
    @Binds
    fun bindChatContactsViewModel(viewModel: ChatContactsViewModel): ViewModel
}