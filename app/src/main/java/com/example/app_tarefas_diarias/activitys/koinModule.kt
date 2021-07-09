package com.example.app_tarefas_diarias.ui

import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.model.AdapterTarefa
import com.example.app_tarefas_diarias.model.ViewModel
import com.example.app_tarefas_diarias.repository.RepositoryTarefas
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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