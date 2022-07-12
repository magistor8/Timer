package com.magistor8.timer.data

import com.magistor8.timer.domain.MainContract

class TimeCalculator {

    fun calculate(state: MainContract.TimerState.Running): Long {
        val currentTimestamp = System.currentTimeMillis()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }

    fun getStringTimeRepresentation(state: MainContract.TimerState): String {
        val elapsedTime = when (state) {
            is MainContract.TimerState.Paused -> state.elapsedTime
            is MainContract.TimerState.Running ->
                calculate(state)
        }
        return format(elapsedTime)

    }

    private fun format(timestamp: Long): String {
        val millisecondsFormatted = (timestamp % 1000).pad(3)
        val seconds = timestamp / 1000
        val secondsFormatted = (seconds % 60).pad(2)
        val minutes = seconds / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hours = minutes / 60
        return if (hours > 0) {
            val hoursFormatted = (minutes / 60).pad(2)
            "$hoursFormatted:$minutesFormatted:$secondsFormatted"
        } else {
            "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength,
        '0')
    companion object {
        const val DEFAULT_TIME = "00:00:000"
    }

}

