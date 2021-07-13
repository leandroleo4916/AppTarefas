package com.example.app_tarefas_diarias.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_tarefas_diarias.entity.EntityTarefa
import com.example.app_tarefas_diarias.repository.RepositoryTarefas
import java.text.FieldPosition

class ViewModel (application: Application): AndroidViewModel(application) {

    private val repository: RepositoryTarefas = RepositoryTarefas.instance(application)

    private val mListTarefa = MutableLiveData<ArrayList<EntityTarefa>>()
    val listTarefa: LiveData<ArrayList<EntityTarefa>> = mListTarefa

    fun getTarefas(){
        val listTarefas = repository.getTarefas()
        mListTarefa.value = listTarefas
    }

    fun setTarefas(complete: String, descrip: String, date: String, hora: String): Boolean {
        return repository.setTarefas(complete, descrip, date, hora)
    }

    fun editTarefas(complete: String, descrip: String, nameNew: String, date: String, hora: String): Boolean {
        return repository.editTarefas(complete, descrip, nameNew, date, hora)
    }

    fun deleteTarefas(descrip: String): Boolean {
        return repository.deleteTarefas(descrip)
    }
}