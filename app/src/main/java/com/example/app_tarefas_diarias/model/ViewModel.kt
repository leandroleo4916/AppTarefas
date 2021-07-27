package com.example.app_tarefas_diarias.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_tarefas_diarias.entity.EntityTarefa
import com.example.app_tarefas_diarias.entity.EntityTarefaDateAndHora
import com.example.app_tarefas_diarias.repository.RepositoryTarefas
import kotlinx.coroutines.*

class ViewModel (private val repository: RepositoryTarefas, application: Application):
    AndroidViewModel(application) {

    private val mListTarefa = MutableLiveData<ArrayList<EntityTarefa>>()
    val listTarefa: LiveData<ArrayList<EntityTarefa>> = mListTarefa

    private val mListTarefaDateAndHora = MutableLiveData<ArrayList<EntityTarefaDateAndHora>>()
    val listTarefaDateAndHora: LiveData<ArrayList<EntityTarefaDateAndHora>> = mListTarefaDateAndHora

    fun getTarefasInit(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            val listTarefas = withContext(Dispatchers.Default) {
                    repository.getTarefas()
                }
            mListTarefa.value = listTarefas
        }
    }

    fun getTarefas(){
        CoroutineScope(Dispatchers.Main).launch {
            val listTarefas = withContext(Dispatchers.Default) {
                repository.getTarefas()
            }
            mListTarefa.value = listTarefas
        }
    }

    fun getTarefasCompleteOrIncomplete(complete: String){
        CoroutineScope(Dispatchers.Main).launch {
            val listTarefas = withContext(Dispatchers.Default) {
                repository.getTarefasCompleteOrIncomplete(complete)
            }
            mListTarefa.value = listTarefas
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