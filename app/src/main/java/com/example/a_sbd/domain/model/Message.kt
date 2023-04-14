package com.example.a_sbd.domain.model

import java.util.*

data class Message(
    val id: Long,
    val text: String,
    val messageType: MessageType,
    val messageDate: Date = Date(),
    val isDeparted: Boolean = false
) {
}