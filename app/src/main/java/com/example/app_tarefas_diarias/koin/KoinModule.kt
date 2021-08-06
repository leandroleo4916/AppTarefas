package com.example.app_tarefas_diarias.koin

import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.model.TasksViewModel
import com.example.app_tarefas_diarias.repository.RepositoryTasks
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataBaseModule = module {
    factory { DataBase(get()) }
}

val repositoryModule = module {
    single { RepositoryTasks(get()) }
}

val viewModelModule = module {
    viewModel { TasksViewModel(get()) }
}

val appModules = listOf(
    repositoryModule, viewModelModule, dataBaseModule
)