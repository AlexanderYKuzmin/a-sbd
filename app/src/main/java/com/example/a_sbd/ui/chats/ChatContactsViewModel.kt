package com.example.a_sbd.ui.chats

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.usecases.*
import java.util.*
import javax.inject.Inject


class ChatContactsViewModel @Inject constructor(
    private val application: Application,
    private val getAllChatContactsUseCase: GetAllChatContactsUseCase,
    private val addContactUseCase: AddContactsUseCase,
    private val addMessageUseCase: InsertMessageUseCase
) : ViewModel() {
    private val listOfContacts = mutableListOf<ChatContact>()
    private val listOfMessages = mutableListOf<Message>()

    val chatContacts: LiveData<List<ChatContact>> = getAllChatContactsUseCase()

    /*init {

        listOfContacts.add(
            ChatContact(
                id = 1L,
                firstName = "Владимир",
                lastName = "Усимов",
                phoneNumber = "+79124409988",
                messages = listOf()
            )
        )
        listOfContacts.add(
            ChatContact(
                id = 2L,
                firstName = "Антон",
                lastName = "Никифоров",
                phoneNumber = "+79125000000",
                messages = listOf()
            )
        )
        listOfContacts.add(
            ChatContact(
                id = 3L,
                firstName = "Александр",
                lastName = "Чухванцев",
                phoneNumber = "+79811500011",
                messages = listOf()
            )
        )

        listOfMessages.add(
            Message(
                id = 1L,
                text = "Hello, what's wrong?",
                messageType = MessageType.START_IN,
                messageDate = getDateOf(2022, Calendar.DECEMBER, 25, 13, 15, 12),
                isDeparted = true,
                3L
            )
        )

        listOfMessages.add(
            Message(
                id = 2L,
                text = "Hi, what do you mean?",
                messageType = MessageType.START_OUT,
                messageDate = getDateOf(2022, Calendar.DECEMBER, 25, 13, 16, 12),
                isDeparted = true,
                3L
            )
        )

        listOfMessages.add(
            Message(
                id = 3L,
                text = "Yellow leaves are falling down!",
                messageType = MessageType.START_IN,
                messageDate = getDateOf(2023, Calendar.MAY, 18, 14, 15, 12),
                isDeparted = true,
                3L
            )
        )

        listOfMessages.add(
            Message(
                id = 4L,
                text = "Wow, wow, wow!!!",
                messageType = MessageType.START_IN,
                messageDate = getDateOf(2023, Calendar.MAY, 19, 20, 15, 12),
                isDeparted = true,
                1L
            )
        )

        listOfMessages.add(
            Message(
                id = 5L,
                text = "Yeah! I got it!",
                messageType = MessageType.START_IN,
                messageDate = getDateOf(2023, Calendar.MAY, 19, 21, 10, 12),
                isDeparted = true,
                2L
            )
        )

        listOfMessages.add(
            Message(
                id = 6L,
                text = "Oh early days come!",
                messageType = MessageType.START_IN,
                messageDate = getDateOf(2023, Calendar.MAY, 20, 21, 10, 12),
                isDeparted = true,
                2L
            )
        )

        viewModelScope.launch {
            addContactUseCase(listOfContacts.toTypedArray())
            listOfMessages.forEach { addMessageUseCase(it) }
            //addMessageUseCase(listOfMessages.toTypedArray())
        }
    }*/

    private fun getDateOf(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(i, i1, i2, i3, i4, i5)
        return calendar.time
    }

}