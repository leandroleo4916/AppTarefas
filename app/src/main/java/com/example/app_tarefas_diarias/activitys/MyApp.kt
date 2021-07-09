package com.example.app_tarefas_diarias.activitys

import android.app.Application
import com.example.app_tarefas_diarias.ui.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(appModules)
        }
    }
}