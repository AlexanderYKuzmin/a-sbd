package com.example.a_sbd

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.a_sbd.data.database.ASBDDatabase
import com.example.a_sbd.data.database.ChatContactsDao
import com.example.a_sbd.data.database.model.MessageDb
import com.example.a_sbd.domain.usecases.ClearDatabaseByDateUseCase.Companion.THREE_DAYS_HOURS
import com.example.a_sbd.extensions.getOrAwaitValue
import com.example.a_sbd.extensions.hours
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

//@RunWith(AndroidJUnit4::class)
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class DatabaseOperationTest {

    private lateinit var db: ASBDDatabase
    private lateinit var dao: ChatContactsDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ASBDDatabase::class.java).build()

        dao = db.chatContactsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private lateinit var listOfMessages: List<MessageDb>

    @Test
    fun clearOldDataTest() {
        var deletedCount: Int = 0
        runBlocking {
            val jobInsert = launch {
                listOfMessages = TestUtil.listOfMessages
                listOfMessages.forEach { dao.insertMessage(it) }
            }

            jobInsert.join()
            val jobDelete = launch {
                val dateInHours = Date().hours()
                Log.d("Test", "dateInHours: $dateInHours")
                deletedCount = dao.deleteOldMessages(dateInHours - THREE_DAYS_HOURS)
            }
        }
        Assert.assertEquals(4, deletedCount)
    }

    @Test
    fun getAllDelayedMessagesTest() {
        populateDb()

        val messagesDelayedLiveData = dao.getMessageDelayed()
        val list = messagesDelayedLiveData.getOrAwaitValue()

        assertThat(list.first().text, CoreMatchers.containsString("test new message_3 IN"))
    }

    @Test
    fun updateMessageById() {
        populateDb()
        runBlocking {
            val jobUpdate = launch {
                dao.updateMessageByIdToDeparted(3L)
            }
        }
        val messagesDelayedLiveData = dao.getMessageDelayed()
        val list = messagesDelayedLiveData.getOrAwaitValue()
        assert(list.isEmpty())
    }

    private fun populateDb() {
        db.clearAllTables()

        runBlocking {
            val jobInsert = launch {
                listOfMessages = TestUtil.listOfMessages
                listOfMessages.forEach { dao.insertMessage(it) }
            }
        }
    }
}
