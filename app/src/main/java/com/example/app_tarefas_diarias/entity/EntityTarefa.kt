package com.example.app_tarefas_diarias.entity

data class EntityTarefa(
    val id: Int,
    val complete: String = "",
    val description: String = "",
    val date: String = "",
    val hora: String = ""
)
