package com.example.app_tarefas_diarias.koin

import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.model.ViewModel
import com.example.app_tarefas_diarias.repository.RepositoryTarefas
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataBaseModule = module {
    factory { DataBase(get()) }
}

val repositoryModule = module {
    single { RepositoryTarefas(get()) }
}

val viewModelModule = module {
    viewModel { ViewModel(get(), get()) }
}

val appModules = listOf(
    repositoryModule, viewModelModule, dataBaseModule
)