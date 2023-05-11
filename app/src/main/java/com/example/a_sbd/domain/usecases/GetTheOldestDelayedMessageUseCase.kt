package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.Message
import javax.inject.Inject

class GetTheOldestDelayedMessageUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    suspend operator fun invoke(): Message? = repository.getMessageDelayed()
}