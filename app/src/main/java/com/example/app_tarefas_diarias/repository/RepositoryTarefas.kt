package com.example.app_tarefas_diarias.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.app_tarefas_diarias.constants.ConstantsTarefa
import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.entity.EntityTarefa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryTarefas(private val mDataBase: DataBase) {

    suspend fun setTarefas(complete: String, descrip: String, date: String, hora: String): Boolean {

        return withContext(Dispatchers.Default) {
            try {
                val db = mDataBase.writableDatabase
                val insertValues = ContentValues()
                insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE, complete)
                insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION, descrip)
                insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.DATE, date)
                insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.HORA, hora)

                db.insert(ConstantsTarefa.TAREFA.TABLE_NAME, null, insertValues)
                true

            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun getTarefas(): ArrayList<EntityTarefa> {

        return withContext(Dispatchers.Default) {
            val tarefa: ArrayList<EntityTarefa> = arrayListOf()

            try {
                val cursor: Cursor
                val db = mDataBase.readableDatabase

                val projection = arrayOf(
                    ConstantsTarefa.TAREFA.COLUNAS.ID,
                    ConstantsTarefa.TAREFA.COLUNAS.COMPLETE,
                    ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION,
                    ConstantsTarefa.TAREFA.COLUNAS.DATE,
                    ConstantsTarefa.TAREFA.COLUNAS.HORA
                )

                cursor = db.query(ConstantsTarefa.TAREFA.TABLE_NAME, projection, null,
                    null, null, null, null)

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.ID))
                        val complete = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE))
                        val description = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION))
                        val date = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.DATE))
                        val hora = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.HORA))

                        tarefa.add(EntityTarefa(id, complete, description, date, hora))
                    }
                }
                cursor?.close()
            } catch (e: Exception) { }
            tarefa
        }
    }

    suspend fun updateTarefas(tarefa: EntityTarefa): Boolean {

        return withContext(Dispatchers.Default) {
            try {
                val db = mDataBase.writableDatabase
                val selection = ConstantsTarefa.TAREFA.COLUNAS.ID + " = ?"
                val args = arrayOf(tarefa.id.toString())
                val updateValues = ContentValues()
                updateValues.put(ConstantsTarefa.TAREFA.COLUNAS.ID, tarefa.id)
                updateValues.put(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE, tarefa.complete)
                updateValues.put(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION, tarefa.description)
                updateValues.put(ConstantsTarefa.TAREFA.COLUNAS.DATE, tarefa.date)
                updateValues.put(ConstantsTarefa.TAREFA.COLUNAS.HORA, tarefa.hora)

                db.update(ConstantsTarefa.TAREFA.TABLE_NAME, updateValues, selection, args)
                true

            } catch (e: Exception) {
                false
            }
        }
    }
}