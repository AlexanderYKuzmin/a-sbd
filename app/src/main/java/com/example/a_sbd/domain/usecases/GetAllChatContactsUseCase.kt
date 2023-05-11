package com.example.a_sbd.domain.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.a_sbd.domain.ASBDRepository
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.ui.MainActivity
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class GetAllChatContactsUseCase @Inject constructor(
    private val repository: ASBDRepository
) {
    operator fun  invoke(): LiveData<List<ChatContact>> {
        val chats = repository.getAll()
        Log.d(TAG, "Use case chats = ${chats.value?.size}")
        return chats
    }
}