package com.example.app_tarefas_diarias.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_tarefas_diarias.entity.EntityTarefa
import com.example.app_tarefas_diarias.repository.RepositoryTarefas
import kotlinx.coroutines.*

class ViewModel (private val mSearchTarefa: RepositoryTarefas, application: Application): AndroidViewModel(application) {

    private val mListTarefa = MutableLiveData<ArrayList<EntityTarefa>>()
    val listTarefa: LiveData<ArrayList<EntityTarefa>> = mListTarefa

    private val mResult = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = mResult

    fun getTarefas(){

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            val listTarefas = withContext(Dispatchers.Default) {
                    mSearchTarefa.getTarefas()
                }
            mListTarefa.value = listTarefas
        }
    }

    fun setTarefas(complete: String, descrip: String, date: String, hora: String) {

        CoroutineScope(Dispatchers.Main).launch {
            val ret = withContext(Dispatchers.Default) {
                mSearchTarefa.setTarefas(complete, descrip, date, hora)
            }
            mResult.value = ret
        }
    }
}