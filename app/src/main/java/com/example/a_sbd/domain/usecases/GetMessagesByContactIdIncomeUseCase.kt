package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import javax.inject.Inject

class GetMessagesByContactIdIncomeUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    suspend operator fun invoke(contactId: Long) = repository.getMessagesByContactIdIncome(contactId)
}