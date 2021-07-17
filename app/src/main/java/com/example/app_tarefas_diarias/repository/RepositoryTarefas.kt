package com.example.app_tarefas_diarias.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.app_tarefas_diarias.constants.ConstantsTarefa
import com.example.app_tarefas_diarias.database.DataBase
import com.example.app_tarefas_diarias.entity.EntityTarefa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryTarefas(private val mDataBase: DataBase) {

    fun setTarefas(complete: String, descrip: String, date: String, hora: String): Boolean {

        return try {
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
                        val id =
                            cursor.getInt(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.ID))
                        val complete =
                            cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE))
                        val description =
                            cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION))
                        val date =
                            cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.DATE))
                        val hora =
                            cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.HORA))

                        tarefa.add(EntityTarefa(id, complete, description, date, hora))
                    }
                }
                cursor?.close()
            } catch (e: Exception) {
            }
            tarefa
        }
    }

    suspend fun getTarefasCompleteOrIncomplete(complete: String): ArrayList<EntityTarefa> {

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
                val selection = ConstantsTarefa.TAREFA.COLUNAS.COMPLETE + " = ?"
                val args = arrayOf(complete)

                cursor = db.query(ConstantsTarefa.TAREFA.TABLE_NAME, projection, selection,
                    args, null, null, null)

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.ID))
                        val completo = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE))
                        val description = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION))
                        val date = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.DATE))
                        val hora = cursor.getString(cursor.getColumnIndex(ConstantsTarefa.TAREFA.COLUNAS.HORA))

                        tarefa.add(EntityTarefa(id, completo, description, date, hora))
                    }
                }
                cursor?.close()
            } catch (e: Exception) {
            }
            tarefa
        }
    }

    fun editTarefas(complete: String, descrip: String, nameNew: String, date: String, hora: String ): Boolean {

        return try {
            val db = mDataBase.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.COMPLETE, complete)
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION, nameNew)
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.DATE, date)
            contentValues.put(ConstantsTarefa.TAREFA.COLUNAS.HORA, hora)

            val selection = ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION + " = ?"
            val args = arrayOf(descrip)

            db.update(ConstantsTarefa.TAREFA.TABLE_NAME, contentValues, selection, args)

            true

        } catch (e: Exception) {
            false
        }
    }

    fun editTarefasComplete(completeCurrent: String, name: String): Boolean {

        return try {
            val db = mDataBase.writableDatabase

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

    fun deleteTarefas(descrip: String): Boolean {

        return try {
            val db = mDataBase.writableDatabase
            val selection = ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION + " = ?"
            val args = arrayOf(descrip)

            db.delete(ConstantsTarefa.TAREFA.TABLE_NAME, selection, args)
            true

        } catch (e: Exception) {
            false
        }
    }

    fun getDescription(name: String): Boolean {

        try {
            val cursor: Cursor
            val db = mDataBase.readableDatabase

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
}