package com.example.a_sbd.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a_sbd.domain.model.ChatContact
import java.util.*
import javax.inject.Inject

class ChatContacts @Inject constructor() {
    private val listOfContacts = mutableListOf<ChatContact>()

    private val _contacts = MutableLiveData<List<ChatContact>>()
    val contacts: LiveData<List<ChatContact>>
    get() = _contacts

    init {
        listOfContacts.add(
            ChatContact(
                id = 1L,
                firstName = "Владимир",
                lastName = "Усимов",
                lastMessage = "Горячих булок больше не будет",
                lastMessageDate = Date(),
                phoneNumber = "+79124409988")
        )
        listOfContacts.add(
            ChatContact(
                id = 2L,
                firstName = "Антон",
                lastName = "Никифоров",
                lastMessage = "Чебурашка не вернулся со стройки",
                lastMessageDate = Date(),
                phoneNumber = "+79125000000")
        )
        listOfContacts.add(
            ChatContact(
                id = 3L,
                firstName = "Александр",
                lastName = "Чухванцев",
                lastMessage = "Суслики в норах больше не сидят",
                lastMessageDate = Date(),
                phoneNumber = "+79811500011")
        )

        _contacts.value = listOfContacts
    }

    fun addContact(contact: ChatContact) {
        listOfContacts.add(contact)
        _contacts.value = listOfContacts
    }
}