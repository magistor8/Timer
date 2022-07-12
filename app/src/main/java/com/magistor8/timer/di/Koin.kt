package com.magistor8.timer.di

import com.magistor8.timer.data.TimeCalculator
import com.magistor8.timer.data.TimerImpl
import com.magistor8.timer.domain.MainContract
import com.magistor8.timer.ui.MainViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {
    factory<MainContract.Timer> { params ->TimerImpl(params.get())}
    single { TimeCalculator() }
    viewModel {MainViewModel()}
}