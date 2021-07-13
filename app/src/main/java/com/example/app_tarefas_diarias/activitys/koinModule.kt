package com.example.app_tarefas_diarias.ui

import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.model.ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataBaseModule = module {
    single { DataBase(get()) }
}

val viewModelModule = module {
    viewModel { ViewModel(get()) }
}

val appModules = listOf(
     viewModelModule, dataBaseModule
)