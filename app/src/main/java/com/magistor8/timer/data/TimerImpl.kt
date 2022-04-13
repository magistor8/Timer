package com.magistor8.timer.data

import com.magistor8.timer.domain.MainContract
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class TimerImpl(override val id: Int) : MainContract.Timer, KoinComponent{

    override var timerState: MainContract.TimerState = MainContract.TimerState.Paused(0)
    private val timeCalculator: TimeCalculator by inject()

    override fun start() {
        if (timerState !is MainContract.TimerState.Running) {
            val elapsedTime = (timerState as MainContract.TimerState.Paused).elapsedTime
            timerState = MainContract.TimerState.Running(
                startTime = System.currentTimeMillis(),
                elapsedTime = elapsedTime
            )
        }
    }

    override fun stop() {
        timerState = MainContract.TimerState.Paused(0)
    }

    override fun pause() {
        if (timerState !is MainContract.TimerState.Paused) {
            val elapsedTime = timeCalculator.calculate(timerState as MainContract.TimerState.Running)
            timerState = MainContract.TimerState.Paused(elapsedTime = elapsedTime)
        }
    }

}