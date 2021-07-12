package com.example.app_tarefas_diarias.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_tarefas_diarias.entity.EntityTarefa
import com.example.app_tarefas_diarias.repository.RepositoryTarefas

class ViewModel (private val repository: RepositoryTarefas, application: Application): AndroidViewModel(application) {

    private val mListTarefa = MutableLiveData<ArrayList<EntityTarefa>>()
    val listTarefa: LiveData<ArrayList<EntityTarefa>> = mListTarefa

    private val mResultSet = MutableLiveData<Boolean>()
    val resultSet: LiveData<Boolean> = mResultSet

    private val mResultEdit = MutableLiveData<Boolean>()
    val resultEdit: LiveData<Boolean> = mResultEdit

    private val mResultDelete = MutableLiveData<Boolean>()
    val resultDelete: LiveData<Boolean> = mResultDelete

    fun getTarefas(){
        val listTarefas = repository.getTarefas()
        mListTarefa.value = listTarefas
    }

    fun setTarefas(complete: String, descrip: String, date: String, hora: String) {
        when {
            repository.setTarefas(complete, descrip, date, hora) -> {
                getTarefas()
                mResultSet.value = true
            }
            else -> { mResultSet.value = false }
        }
    }

    fun editTarefas(complete: String, descrip: String, date: String, hora: String) {
        when {
            repository.editTarefas(complete, descrip, date, hora) -> {
                getTarefas()
                mResultEdit.value = true
            }
            else -> { mResultEdit.value = false }
        }
    }

    fun deleteTarefas(descrip: String) {
        when {
            repository.deleteTarefas(descrip) -> {
                getTarefas()
                mResultDelete.value = true
            }
            else -> { mResultDelete.value = false }
        }
    }
}