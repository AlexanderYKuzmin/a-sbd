package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import javax.inject.Inject

class GetAllMessagesUnsentUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    suspend operator fun invoke() = repository.getMessageDelayed()
}