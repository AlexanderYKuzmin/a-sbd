package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.ChatContact
import javax.inject.Inject

class AddContactsUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    suspend operator fun invoke(contacts: Array<ChatContact>) = repository.addContacts(contacts)
}