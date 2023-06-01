package com.example.a_sbd.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a_sbd.data.database.model.ChatContactDb
import com.example.a_sbd.data.database.model.ChatContactWithMessages
import com.example.a_sbd.data.database.model.MessageDb
import com.example.a_sbd.domain.model.Message
import java.sql.Date

@Dao
interface ChatContactsDao {

    @Query("SELECT * FROM contacts")
    fun getAll(): LiveData<List<ChatContactWithMessages>>

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): LiveData<List<ChatContactDb>>

    @Query("SELECT * FROM contacts WHERE id == :id")
    fun getContactWithMessagesById(id: Long): LiveData<ChatContactWithMessages>

    @Query("SELECT * FROM messages WHERE id == :id")
    fun getMessage(id: Long): MessageDb?

    @Query("SELECT * FROM messages WHERE contact_id = :contactId ORDER BY id DESC LIMIT 1")
    fun getMessageByContactIdLast(contactId: Long): MessageDb?

    //@Query("SELECT * FROM messages WHERE id == (SELECT MIN(id) FROM messages WHERE is_departed == 0)")
    @Query("SELECT * FROM messages WHERE is_departed == 0 ORDER BY id")
    fun getMessageDelayed(): LiveData<List<MessageDb>>

    @Query("SELECT * FROM messages WHERE contact_id == :contactId")
    fun getMessages(contactId: Long): LiveData<List<MessageDb>>

    @Query("SELECT * FROM messages WHERE contact_id =:contactId AND (type = 'start_in' OR type = 'normal_in')")
    suspend fun getMessagesByContactIdIncome(contactId: Long): List<MessageDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(chatContactDbs: Array<ChatContactDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messageDbs: Array<MessageDb>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageDb: MessageDb): Long

    @Update
    suspend fun updateMessage(messageDb: MessageDb): Int

    @Query("UPDATE messages SET is_departed = 1 WHERE id = :id")
    suspend fun updateMessageByIdToDeparted(id: Long): Int

    @Delete
    fun deleteContactWithMessages(contactDb: ChatContactDb)

    @Delete
    fun deleteMessage(messageDb: MessageDb)

    @Query("DELETE FROM messages WHERE date_h < :oldDate")
    suspend fun deleteOldMessages(oldDate: Int): Int
}