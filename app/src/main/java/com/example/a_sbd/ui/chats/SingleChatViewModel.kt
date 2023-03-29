package com.example.a_sbd.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingleChatViewModel : ViewModel() {

    private val _isSingleChatStarted = MutableLiveData<Boolean>()
    val isSingleChatStarted: LiveData<Boolean>
    get() = _isSingleChatStarted

    init {
        _isSingleChatStarted.value = true
    }
}