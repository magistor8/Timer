package com.magistor8.timer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magistor8.timer.data.TimeCalculator
import com.magistor8.timer.domain.MainContract
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class MainViewModel : ViewModel(), MainContract.MyViewModelInterface, KoinComponent {

    companion object {
        const val DELAY : Long = 20
    }

    private val _viewState: MutableStateFlow<MutableMap<Int, String>> = MutableStateFlow(mutableMapOf())
    private val timeCalculator: TimeCalculator by inject()

    override var timerCount: Int = 1
    override val timers: MutableList<MainContract.Timer> = mutableListOf()
    override val viewState: StateFlow<Map<Int, String>> = _viewState

    init {
        //Инжектим таймеры
        for (i in 1..3) {
            val timer : MainContract.Timer by inject { parametersOf(i)}
            timers.add(timer)
        }

        //Цикл по таймерам
        viewModelScope.launch {
            while (isActive) {
                _viewState.value = mutableMapOf()
                for (timer in timers) {
                    _viewState.value[timer.id] = timeCalculator.getStringTimeRepresentation(timer.timerState)
                }
                delay(DELAY)
            }
        }
    }

    override fun onEvent(event: MainContract.Event) {
        when(event) {
            is MainContract.Event.Start -> startTimer(event.id)
            is MainContract.Event.Pause -> pauseTimer(event.id)
            is MainContract.Event.Stop -> stopTimer(event.id)
        }
    }

    private fun pauseTimer(id: Int) {
        for (timer in timers) {
            if (timer.id == id) {timer.pause()}
        }
    }

    private fun stopTimer(id: Int) {
        for (timer in timers) {
            if (timer.id == id) {timer.stop()}
        }
    }

    private fun startTimer(id: Int) {
        for (timer in timers) {
            if (timer.id == id) {timer.start()}
        }
    }

}