package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType
import java.util.*
import javax.inject.Inject

class InsertMessageByTextUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    suspend operator fun invoke(
        text: String,
        messageType: MessageType,
        contactId: Long
    ) = repository.addMessage(
        Message(
            id = 0L,
            text = text,
            messageType = messageType,
            messageDate = Date(),
            contactId = contactId
        )
    )
}