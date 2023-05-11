package com.example.a_sbd.domain

import androidx.lifecycle.LiveData
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.domain.model.Message

interface ASBDRepository {

    fun getAll(): LiveData<List<ChatContact>>

    fun getAllContacts(): LiveData<List<ChatContact>>

    fun getAllContactsWithLastMessages(): LiveData<List<ChatContact>>

    fun getContact(id: Long): LiveData<ChatContact>

    suspend fun getMessage(id: Long): Message?

    suspend fun getMessageDelayed(): LiveData<List<Message>>

    fun getMessages(contactId: Long): LiveData<List<Message>>

    suspend fun updateMessage(message: Message): Int

    suspend fun updateMessageByIdToDeparted(id: Long): Int

    suspend fun addContacts(contacts: Array<ChatContact>)

    suspend fun addMessages(messages: Array<Message>): List<Long>

    suspend fun addMessage(message: Message): Long
}