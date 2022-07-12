package com.magistor8.timer

import android.app.Application
import com.magistor8.timer.di.myModule
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(myModule)
        }
    }
}