package com.example.a_sbd.domain.usecases

import androidx.lifecycle.LiveData
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.Message
import javax.inject.Inject

class GetAllMessagesOfContactUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    operator fun invoke(contactId: Long): LiveData<List<Message>> {
        return repository.getMessages(contactId)
    }
}
