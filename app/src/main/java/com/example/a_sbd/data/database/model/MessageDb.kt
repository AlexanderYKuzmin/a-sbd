package com.example.a_sbd.data.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.a_sbd.domain.model.MessageType
import java.sql.Date
import java.time.DateTimeException

@Entity(tableName = "messages")
data class MessageDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val text: String,

    val type: String,

    val date: String,

    @ColumnInfo(name = "date_h")
    val dateHours: Int,

    @ColumnInfo(name = "is_departed")
    val isDeparted: Int,

    @ColumnInfo(name = "contact_id")
    val contactId: Long
)