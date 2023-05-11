package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.Message
import javax.inject.Inject

class InsertMessagesUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    //suspend operator fun invoke(messages: Array<Message>) = repository.addMessages(messages)
    suspend operator fun invoke(message: Message) = repository.addMessage(message)
}