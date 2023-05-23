package com.example.a_sbd.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a_sbd.domain.usecases.InsertMessageUseCase
import com.example.a_sbd.domain.usecases.GetAllMessagesOfContactUseCase
import com.example.a_sbd.ui.chats.SingleChatViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>,
    private val getAllMessagesOfContactUseCase: GetAllMessagesOfContactUseCase,
    private val insertMessagesUseCase: InsertMessageUseCase
) : ViewModelProvider.Factory {

    var contactId: Long = -1L

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (contactId == -1L) viewModelProviders[modelClass]?.get() as T
        else SingleChatViewModel (
            getAllMessagesOfContactUseCase,
            insertMessagesUseCase,
            contactId
        ) as T
    }
}