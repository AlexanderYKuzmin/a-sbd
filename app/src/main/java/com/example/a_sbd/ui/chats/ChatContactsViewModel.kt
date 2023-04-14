package com.example.a_sbd.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.domain.usecases.GetAllChatContactsUseCase
import javax.inject.Inject


class ChatContactsViewModel @Inject constructor(
    private val getAllChatContactsUseCase: GetAllChatContactsUseCase
) : ViewModel() {

    val chatContacts: LiveData<List<ChatContact>> = getAllChatContactsUseCase()

    init {

    }
    /*private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }*/


    //val text: LiveData<String> = _text

}