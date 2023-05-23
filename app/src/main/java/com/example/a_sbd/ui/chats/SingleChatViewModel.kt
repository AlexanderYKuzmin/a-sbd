package com.example.a_sbd.ui.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType.*
import com.example.a_sbd.domain.usecases.InsertMessageUseCase
import com.example.a_sbd.domain.usecases.GetAllMessagesOfContactUseCase
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.launch
import java.util.*

class SingleChatViewModel (
    getAllMessagesOfContactUseCase: GetAllMessagesOfContactUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val contactId: Long
) : ViewModel() {

    private val _messageSentLiveData = MutableLiveData<Message>()
    val messageSentLiveData: LiveData<Message>
        get() = _messageSentLiveData

    /*private val _isSingleChatStarted = MutableLiveData<Boolean>()
    val isSingleChatStarted: LiveData<Boolean>
    get() = _isSingleChatStarted*/
    //private var contactId: Long = -1

    val messages = getAllMessagesOfContactUseCase(contactId)

    fun insertMessageToDb(text: String) {
        val lastMessage = messages.value?.sortedWith(compareBy(Message::messageDate))?.last()
        //val messagesToAdd = mutableListOf<Message>()
        //messagesToAdd.add(
        val messageToAdd = Message(
                0L,
                text,
                if (lastMessage?.messageType == START_OUT || lastMessage?.messageType == NORMAL_OUT)
                    NORMAL_OUT else START_OUT,
                Date(),
                false,
                contactId
            )
        //)

        viewModelScope.launch {
            val id = insertMessageUseCase(messageToAdd)
            Log.d(TAG, "Message inserted. Id: $id")
            _messageSentLiveData.value = messageToAdd.copy(id = id)
        }
    }
}