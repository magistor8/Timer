package com.magistor8.timer.domain

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MainContract {

    interface Timer {
        val id: Int
        var timerState: TimerState
        fun start()
        fun stop()
        fun pause()
    }

    sealed interface TimerState {
        data class Paused(val elapsedTime: Long) : TimerState
        data class Running(
            val startTime: Long,
            val elapsedTime: Long
        ) : TimerState
    }

    sealed interface Event {
        data class Start(val id: Int) : Event
        data class Stop(val id: Int) : Event
        data class Pause(val id: Int) : Event
    }

    interface MyViewModelInterface {
        val viewState: StateFlow<Map<Int, String>>
        val timers : MutableList<Timer>
        var timerCount : Int
        fun onEvent(event: Event)
    }

}