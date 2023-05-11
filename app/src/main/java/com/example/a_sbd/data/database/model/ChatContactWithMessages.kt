package com.example.a_sbd.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChatContactWithMessages(
    @Embedded
    val contact: ChatContactDb,
    @Relation(
        parentColumn = "id",
        entityColumn = "contact_id"
    )
    val messages: List<MessageDb>
)