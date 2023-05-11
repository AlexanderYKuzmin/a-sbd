package com.example.a_sbd.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType
import java.util.*

class MessageContainer {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>>
        get() = _messages

    /*init {
        val list = mutableListOf<Message>()

        list.add(
            Message(
                1L,
                "Hi, dude! How r u?",
                MessageType.START_IN,
                getDateOf(2023, Calendar.APRIL, 20, 10, 12, 35),
                false
            )
        )

        list.add(
            Message(
                2L,
                "I'm OK.",
                MessageType.START_IN,
                getDateOf(2023, Calendar.APRIL, 20, 10, 15, 35),
                true
            )
        )


        _messages.value = list
    }*/

    private fun getDateOf(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(i, i1, i2, i3, i4, i5)
        return calendar.time
    }
}