package com.example.app_tarefas_diarias.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_tarefas_diarias.entity.EntityTarefa
import com.example.app_tarefas_diarias.entity.EntityTarefaDateAndHora
import com.example.app_tarefas_diarias.repository.RepositoryTarefas
import kotlinx.coroutines.*

class TarefasViewModel (private val repository: RepositoryTarefas): ViewModel() {

    private val vListTarefa = MutableLiveData<ArrayList<EntityTarefa>>()
    val listTarefa: LiveData<ArrayList<EntityTarefa>> = vListTarefa

    private val mListTarefaDateAndHora = MutableLiveData<ArrayList<EntityTarefaDateAndHora>>()
    val listTarefaDateAndHora: LiveData<ArrayList<EntityTarefaDateAndHora>> = mListTarefaDateAndHora

    fun getTarefasInit(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            val listTarefas = withContext(Dispatchers.Default) {
                    repository.getTarefas()
                }
            vListTarefa.value = listTarefas
        }
    }

    fun getTarefas(){
        CoroutineScope(Dispatchers.Main).launch {
            val listTarefas = withContext(Dispatchers.Default) {
                repository.getTarefas()
            }
            vListTarefa.value = listTarefas
        }
    }

    fun getTarefasCompleteOrIncomplete(complete: String){
        CoroutineScope(Dispatchers.Main).launch {
            val listTarefas = withContext(Dispatchers.Default) {
                repository.getTarefasCompleteOrIncomplete(complete)
            }
            vListTarefa.value = listTarefas
        }
    }

    fun getTarefasDateAndHora(complete: String){
        CoroutineScope(Dispatchers.Main).launch {
            val listTarefasDateAndHora = withContext(Dispatchers.Default) {
                repository.getTarefasDateAndHora(complete)
            }
            mListTarefaDateAndHora.value = listTarefasDateAndHora
        }
    }

    fun getDescription(name: String): Boolean {
        return repository.getDescription(name)
    }

    fun setTarefas(complete: String, descrip: String, date: String, hora: String): Boolean {
        return repository.setTarefas(complete, descrip, date, hora)
    }

    fun editTarefas(complete: String, descrip: String, nameNew: String, date: String,
                    hora: String): Boolean {
        return repository.editTarefas(complete, descrip, nameNew, date, hora)
    }

    fun editTarefasComplete(completeCurrent: String, name: String): Boolean {
        return repository.editTarefasComplete(completeCurrent, name)
    }

    fun deleteTarefas(descrip: String): Boolean {
        return repository.deleteTarefas(descrip)
    }
}