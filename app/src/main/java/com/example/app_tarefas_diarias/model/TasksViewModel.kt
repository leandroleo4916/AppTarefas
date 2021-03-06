package com.example.app_tarefas_diarias.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_tarefas_diarias.entity.EditTask
import com.example.app_tarefas_diarias.entity.EntityTask
import com.example.app_tarefas_diarias.entity.EntityTaskDateAndHora
import com.example.app_tarefas_diarias.interfaces.ITaskListener
import com.example.app_tarefas_diarias.interfaces.ITaskViewModel
import com.example.app_tarefas_diarias.repository.RepositoryTasks
import kotlinx.coroutines.*

class TasksViewModel (private val repository: RepositoryTasks): ViewModel(), ITaskViewModel {

    private val vListTask = MutableLiveData<ArrayList<EntityTask>>()
    val listTask: LiveData<ArrayList<EntityTask>> = vListTask

    private val vListTaskDateAndHora = MutableLiveData<ArrayList<EntityTaskDateAndHora>>()
    val listTaskDateAndHora: LiveData<ArrayList<EntityTaskDateAndHora>> = vListTaskDateAndHora

    override fun getTask(){
        CoroutineScope(Dispatchers.Main).launch {
            val listTasks = withContext(Dispatchers.Default) {
                repository.getTasks()
            }
            vListTask.value = listTasks
        }
    }

    fun getTaskCompleteOrIncomplete(complete: String){
        CoroutineScope(Dispatchers.Main).launch {
            val listTasks = withContext(Dispatchers.Default) {
                repository.getTasksCompleteOrIncomplete(complete)
            }
            vListTask.value = listTasks
        }
    }

    override fun getTaskDateAndHora(complete: String){
        CoroutineScope(Dispatchers.Main).launch {
            val listTasksDateAndHora = withContext(Dispatchers.Default) {
                repository.getTasksDateAndHora(complete)
            }
            vListTaskDateAndHora.value = listTasksDateAndHora
        }
    }

    fun getDescription(name: String): Boolean {
        return repository.getDescription(name)
    }

    override fun setTasks(entityTask: EntityTask): Boolean {
        return repository.setTask(entityTask)
    }

    override fun editTasks(editTask: EditTask): Boolean {
        return repository.editTasks(editTask)
    }

    override fun editTasksComplete(completeCurrent: String, name: String): Boolean {
        return repository.editTasksComplete(completeCurrent, name)
    }

    override fun deleteTasks(description: String): Boolean {
        return repository.deleteTask(description)
    }
}