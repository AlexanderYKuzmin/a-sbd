package com.example.a_sbd.di

import com.example.a_sbd.ui.chats.ChatContactsFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface ChatSubComponent {

    fun inject(chatContactsFragment: ChatContactsFragment)
}