package com.example.a_sbd.domain.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class GetAllMessagesUnsentUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    suspend operator fun invoke(): LiveData<List<Message>> {
        Log.d(TAG, "GetAll unsent messages use case")
        val liveData = repository.getMessageDelayed()

        return liveData
    }
}