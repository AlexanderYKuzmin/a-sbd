package com.example.a_sbd.domain.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class DepartReminderUseCase(
    private val timerScope: CoroutineScope
) {
    private val _isTimeToStartSession = MutableLiveData(false)
    val isTimeToStartSession: LiveData<Boolean> = _isTimeToStartSession

    private var job: Job? = null

    fun startTimer(duration: Int) {
        if (job?.isActive == true && job != null) {
            job?.cancel()
        }

        job = timerScope.launch {
            Log.d(TAG, "Timer is started")
            delay(duration.minutes)
            _isTimeToStartSession.postValue(true)
        }
    }

    fun resetTimer() {
        job?.cancel()
        job = null
        _isTimeToStartSession.postValue(false)
    }
    /*private var isTimeToStartSession = false

    private var _timerStateFlow = MutableStateFlow(isTimeToStartSession)
    val timerStateFlow: StateFlow<Boolean> = _timerStateFlow

    private var job: Job? = null

    fun toggleTime(totalMinutes: Int) {
        if (job == null) {
            job = timerScope.launch {
                initTimer(totalMinutes)
                    .onCompletion { _timerStateFlow.emit(isTimeToStartSession) }
                    .collect { _timerStateFlow.emit(it) }
            }
        } else {
            job?.cancel()
            job = null
        }
    }

    private fun initTimer(totalMinutes: Int): Flow<Boolean> {
        //        generateSequence(totalSeconds - 1 ) { it - 1 }.asFlow()
        return (totalMinutes - 1  downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
            .onEach { delay(1.minutes) } // Each second later emit a number
            .onStart { emit(totalMinutes) } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingMinutes: Int ->
                if (remainingMinutes == 0) isTimeToStartSession = true
            }
    }*/
}