package com.example.a_sbd.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "contacts")
data class ChatContactDb(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "phone")
    val phoneNumber: String,
)