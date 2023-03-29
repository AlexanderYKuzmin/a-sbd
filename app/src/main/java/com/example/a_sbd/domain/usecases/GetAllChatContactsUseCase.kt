package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import javax.inject.Inject

class GetAllChatContactsUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    operator fun  invoke() = repository.getContacts()
}