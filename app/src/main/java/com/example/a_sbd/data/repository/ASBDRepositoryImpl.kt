package com.example.a_sbd.data.repository

import androidx.lifecycle.LiveData
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.util.ChatContacts
import javax.inject.Inject

class ASBDoRepositoryImpl @Inject constructor() : ASBDRepository {

    @Inject
    lateinit var chatContacts: ChatContacts

    override fun getContacts(): LiveData<List<ChatContact>> {
        return chatContacts.contacts
    }

    override fun getContact(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getMessages(contactId: Long) {
        TODO("Not yet implemented")
    }
}