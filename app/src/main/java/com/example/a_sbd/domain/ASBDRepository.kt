package com.example.a_sbd.domain

import androidx.lifecycle.LiveData
import com.example.a_sbd.domain.model.ChatContact

interface ASBDRepository {

    fun getContacts(): LiveData<List<ChatContact>>

    fun getContact(id: Long)

    fun getMessages(contactId: Long)
}