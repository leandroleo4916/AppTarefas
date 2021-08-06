package com.example.app_tarefas_diarias.entity

data class EntityTask(
    val id: Int,
    val complete: String = "",
    val description: String = "",
    val date: String = "",
    val hora: String = ""
)

data class EditTask(
    val complete: String = "",
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val hora: String = ""
)
