package com.example.a_sbd

import com.example.a_sbd.data.database.model.MessageDb
import com.example.a_sbd.extensions.formatToDateTimeUpToSeconds
import java.util.*

class TestUtil {



    companion object {
        val listOfMessages = listOf<MessageDb>(
            MessageDb(
                text = "test old message_1 IN",
                type = "start_in",
                date = getDateOf(2022, Calendar.DECEMBER, 25, 13, 15, 12).formatToDateTimeUpToSeconds(),
                dateHours = getDateInHours(2022, Calendar.DECEMBER, 25, 13, 15, 12),
                isDeparted = 1,
                contactId = 3L
            ),

            MessageDb(
                text = "test old message_2 OUT",
                type = "start_out",
                date = getDateOf(2022, Calendar.DECEMBER, 26, 13, 15, 12).formatToDateTimeUpToSeconds(),
                dateHours = getDateInHours(2022, Calendar.DECEMBER, 26, 13, 15, 12),
                isDeparted = 1,
                contactId = 3L
            ),

            MessageDb(
                text = "test new message_3 IN",
                type = "start_in",
                date = getDateOf(2023, Calendar.MAY, 20, 13, 15, 12).formatToDateTimeUpToSeconds(),
                dateHours = getDateInHours(2023, Calendar.MAY, 20, 13, 15, 12),
                isDeparted = 0,
                contactId = 3L
            ),

            MessageDb(
                text = "test old message_4 IN",
                type = "start_in",
                date = getDateOf(2022, Calendar.MAY, 27, 13, 15, 12).formatToDateTimeUpToSeconds(),
                dateHours = getDateInHours(2022, Calendar.MAY, 27, 13, 15, 12),
                isDeparted = 1,
                contactId = 2L
            ),

            MessageDb(
                text = "test new message_5 IN",
                type = "start_in",
                date = getDateOf(2023, Calendar.MAY, 20, 14, 15, 12).formatToDateTimeUpToSeconds(),
                dateHours = getDateInHours(2023, Calendar.MAY, 20, 14, 15, 12),
                isDeparted = 1,
                contactId = 2L
            ),

            MessageDb(
                text = "test old message_6 IN",
                type = "start_in",
                date = getDateOf(2022, Calendar.MAY, 28, 14, 15, 12).formatToDateTimeUpToSeconds(),
                dateHours = getDateInHours(2022, Calendar.MAY, 28, 14, 15, 12),
                isDeparted = 1,
                contactId = 1L
            ),
        )

        private fun getDateOf(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): java.util.Date {
            val calendar = Calendar.getInstance()
            calendar.set(i, i1, i2, i3, i4, i5)
            return calendar.time
        }

        private fun getDateInHours(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.set(i, i1, i2, i3, i4, i5)
            return (calendar.time.time / (3600 * 1000)).toInt()
        }
    }
}