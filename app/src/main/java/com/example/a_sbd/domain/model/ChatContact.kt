package com.example.a_sbd.domain.model

import java.util.Date

data class ChatContact(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val lastMessage: String = "",
    val lastMessageDate: Date = Date()
)
