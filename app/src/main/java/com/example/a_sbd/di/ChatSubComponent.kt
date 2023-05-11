package com.example.a_sbd.di

import com.example.a_sbd.ui.chats.ChatContactsFragment
import com.example.a_sbd.ui.chats.SingleChatFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface ChatSubComponent {

    fun inject(chatContactsFragment: ChatContactsFragment)

    fun inject(singleChatFragment: SingleChatFragment)
}