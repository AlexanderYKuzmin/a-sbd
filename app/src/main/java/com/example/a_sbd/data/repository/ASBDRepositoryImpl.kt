package com.example.a_sbd.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.a_sbd.data.database.ChatContactsDao
import com.example.a_sbd.data.mapper.TransformationMapper
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.extensions.hours
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import java.sql.Date
import javax.inject.Inject

class ASBDoRepositoryImpl @Inject constructor(
    private val chatContactsDao: ChatContactsDao
) : ASBDRepository {

    /*@Inject
    lateinit var chatContacts: ChatContacts*/

    private val mapper = TransformationMapper()

    override fun getAll(): LiveData<List<ChatContact>> {
        return Transformations.map(chatContactsDao.getAll()) { list ->
            Log.d(TAG, "Trasformation map. List size: ${list.size}")
            list.map {
                Log.d(TAG, "Trasformation map")
                mapper.mapContactDbWithMessagesDbToChatContact(it)
            }
        }
    }

    override fun getAllContacts(): LiveData<List<ChatContact>> {
        return Transformations.map(chatContactsDao.getAllContacts()) { list ->
            list.map {
                mapper.mapContactDbToEntity(it)
            }
        }
    }

    override fun getAllContactsWithLastMessages(): LiveData<List<ChatContact>> {
        return Transformations.map(chatContactsDao.getAllContacts()) { list ->
            list.map {
                mapper.mapContactDbToEntity(it)
            }
        }
    }

    override fun getContact(id: Long): LiveData<ChatContact> {
        return Transformations.map(chatContactsDao.getContactWithMessagesById(id)) {
            mapper.mapContactDbWithMessagesDbToChatContact(it)
        }
    }

    override suspend fun getMessage(id: Long): Message? {
        return chatContactsDao.getMessage(id)?.let {
            mapper.mapMessageDbToEntity(it)
        }
    }

    override suspend fun getMessageByContactIdLast(contactId: Long): Message? {
        return chatContactsDao.getMessageByContactIdLast(contactId)?.let {
            mapper.mapMessageDbToEntity(it)
        }
    }

    override suspend fun getMessageDelayed(): LiveData<List<Message>> {
        /*val getAll = chatContactsDao.getAll()
        val getAllValue = getAll.value
        getAllValue?.forEach {
            Log.d(TAG, "ASBDRepository: get all : contact_id = ${it.contact.id}, ${it.messages.first()}")
        }*/
        val liveData = chatContactsDao.getMessageDelayed()
        Log.d(TAG, "ASBDRepository: get all delayed messages: ${liveData}")
        return Transformations.map(chatContactsDao.getMessageDelayed()) { list ->
            list.map {
                mapper.mapMessageDbToEntity(it)
            }
        }
    }

    override fun getMessages(contactId: Long): LiveData<List<Message>> {
        return Transformations.map(chatContactsDao.getMessages(contactId)) { list ->
            list.map {
                mapper.mapMessageDbToEntity(it)
            }
        }
    }

    override suspend fun getMessagesByContactIdIncome(contactId: Long): List<Message> {
        return chatContactsDao.getMessagesByContactIdIncome(contactId).map {
            mapper.mapMessageDbToEntity(it)
        }
    }

    override suspend fun updateMessage(message: Message): Int {
        return chatContactsDao.updateMessage(
            mapper.mapMessageToMessageDb(message)
        )
    }

    override suspend fun updateMessageByIdToDeparted(id: Long): Int {
        return chatContactsDao.updateMessageByIdToDeparted(id)
    }

    override suspend fun addContacts(contacts: Array<ChatContact>) {
        chatContactsDao.insertContacts(mapper.mapContactsToContactDbs(contacts))
    }

    override suspend fun addMessages(messages: Array<Message>): List<Long> {
        return chatContactsDao.insertMessages(mapper.mapMessagesToMessageDbs(messages))
    }

    override suspend fun addMessage(message: Message): Long {
        return chatContactsDao.insertMessage(mapper.mapMessageToMessageDb(message))
    }

    override suspend fun deleteOldMessages(oldDateHours: Int): Int {
        return chatContactsDao.deleteOldMessages(oldDateHours)
    }
}