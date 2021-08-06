package com.example.app_tarefas_diarias.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.app_tarefas_diarias.constants.ConstantsTarefa
import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.entity.EditTask
import com.example.app_tarefas_diarias.entity.EntityTask
import com.example.app_tarefas_diarias.entity.EntityTarefaDateAndHora
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class RepositoryTarefas(private val dataBase: DataBase) {

    fun setTask(entityTask: EntityTask): Boolean {

        return try {
            val db = dataBase.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE, entityTask.complete)
            insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION, entityTask.description)
            insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.DATE, entityTask.date)
            insertValues.put(ConstantsTarefa.TAREFA.COLUNAS.HORA, entityTask.hora)

            db.insert(ConstantsTarefa.TAREFA.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTasks(): ArrayList<EntityTask> {

        return withContext(Dispatchers.Default) {

            val task: ArrayList<EntityTask> = arrayListOf()
            try {
                val cursor: Cursor
                val db = dataBase.readableDatabase

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
                        val id = cursor.getInt(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.ID))
                        val complete = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.COMPLETE))
                        val description = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION))
                        val date = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.DATE))
                        val hora = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.HORA))

                        task.add(EntityTask(id, complete, description, date, hora))
                    }
                }
                cursor?.close()
            } catch (e: Exception) {
            }
            task
        }
    }

    suspend fun getTasksCompleteOrIncomplete(complete: String): ArrayList<EntityTask> {

        return withContext(Dispatchers.Default) {

            val task: ArrayList<EntityTask> = arrayListOf()
            try {
                val cursor: Cursor
                val db = dataBase.readableDatabase

                val projection = arrayOf(
                    ConstantsTarefa.TAREFA.COLUNAS.ID,
                    ConstantsTarefa.TAREFA.COLUNAS.COMPLETE,
                    ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION,
                    ConstantsTarefa.TAREFA.COLUNAS.DATE,
                    ConstantsTarefa.TAREFA.COLUNAS.HORA
                )
                val selection = ConstantsTarefa.TAREFA.COLUNAS.COMPLETE + " = ?"
                val args = arrayOf(complete)

                cursor = db.query(ConstantsTarefa.TAREFA.TABLE_NAME, projection, selection,
                    args, null, null, null)

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.ID))
                        val completo = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.COMPLETE))
                        val description = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION))
                        val date = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.DATE))
                        val hora = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.HORA))

                        task.add(EntityTask(id, completo, description, date, hora))
                    }
                }
                cursor?.close()
            } catch (e: Exception) {
            }
            task
        }
    }

    fun editTasks(editTask: EditTask): Boolean {

        return try {
            val db = dataBase.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE, editTask.complete)
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION, editTask.name)
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.DATE, editTask.date)
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.HORA, editTask.hora)

            val selection = ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION + " = ?"
            val args = arrayOf(editTask.description)

            db.update(ConstantsTarefa.TAREFA.TABLE_NAME, contentValues, selection, args)

            true

        } catch (e: Exception) {
            false
        }
    }

    fun editTasksComplete(completeCurrent: String, name: String): Boolean {

        return try {
            val db = dataBase.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE, completeCurrent)

            val selection = ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION + " = ?"
            val args = arrayOf(name)

            db.update(ConstantsTarefa.TAREFA.TABLE_NAME, contentValues, selection, args)
            true

        } catch (e: Exception) {
            false
        }
    }

    fun deleteTask(description: String): Boolean {

        return try {
            val db = dataBase.writableDatabase
            val selection = ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION + " = ?"
            val args = arrayOf(description)

            db.delete(ConstantsTarefa.TAREFA.TABLE_NAME, selection, args)
            true

        } catch (e: Exception) {
            false
        }
    }

    fun getDescription(name: String): Boolean {

        try {
            val cursor: Cursor
            val db = dataBase.readableDatabase

            val projection = arrayOf(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION)
            val selection = ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION + " = ?"
            val args = arrayOf(name)

            cursor = db.query(ConstantsTarefa.TAREFA.TABLE_NAME, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {

                return true
            }
            cursor?.close()

        } catch (e: Exception) {
            return false
        }
        return false
    }

    suspend fun getTasksDateAndHora(complete: String): ArrayList<EntityTarefaDateAndHora> {

        return withContext(Dispatchers.Default) {

            val tarefa: ArrayList<EntityTarefaDateAndHora> = arrayListOf()
            try {
                val cursor: Cursor
                val db = dataBase.readableDatabase

                val projection = arrayOf(
                    ConstantsTarefa.TAREFA.COLUNAS.DATE,
                    ConstantsTarefa.TAREFA.COLUNAS.HORA
                )
                val selection = ConstantsTarefa.TAREFA.COLUNAS.COMPLETE + " = ?"
                val args = arrayOf(complete)

                cursor = db.query(ConstantsTarefa.TAREFA.TABLE_NAME, projection, selection,
                    args, null, null, null)

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val date = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.DATE))
                        val hora = cursor.getString(cursor.getColumnIndex(
                            ConstantsTarefa.TAREFA.COLUNAS.HORA))

                        tarefa.add(EntityTarefaDateAndHora(date, hora))
                    }
                }
                cursor?.close()
            } catch (e: Exception) {
            }
            tarefa
        }
    }
}