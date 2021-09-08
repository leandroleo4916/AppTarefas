package com.example.app_tarefas_diarias.interfaces

import androidx.lifecycle.LiveData
import com.example.app_tarefas_diarias.entity.EditTask
import com.example.app_tarefas_diarias.entity.EntityTask
import com.example.app_tarefas_diarias.entity.EntityTaskDateAndHora

interface ITaskListener {
    fun getDescription(name: String): Boolean
    fun setTask(entityTask: EntityTask): Boolean
    suspend fun getTasks(): ArrayList<EntityTask>
    suspend fun getTasksCompleteOrIncomplete(complete: String): ArrayList<EntityTask>
    fun editTasks(editTask: EditTask): Boolean
    fun editTasksComplete(completeCurrent: String, name: String): Boolean
    fun deleteTask(description: String): Boolean
    suspend fun getTasksDateAndHora(complete: String): ArrayList<EntityTaskDateAndHora>
}

interface ITaskViewModel {
    fun getTask()
    fun getTaskDateAndHora(complete: String)
    fun setTasks(entityTask: EntityTask): Boolean
    fun editTasks(editTask: EditTask): Boolean
    fun editTasksComplete(completeCurrent: String, name: String): Boolean
    fun deleteTasks(description: String): Boolean
}