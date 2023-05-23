package com.example.a_sbd.domain.usecases

import com.example.a_sbd.domain.ASBDRepository
import java.sql.Date
import javax.inject.Inject

class ClearDatabaseByDateUseCase @Inject constructor(
    private val repository: ASBDRepository,
) {

    suspend operator fun invoke(oldDateHours: Int) = repository.deleteOldMessages(oldDateHours)


    companion object {
        const val THREE_DAYS_HOURS = 72
    }
}

