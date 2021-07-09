package com.example.app_tarefas_diarias.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.app_tarefas_diarias.constants.ConstantsTarefa

class DataBase(context: Context) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "tarefa.db"
        private const val DATA_VERSION: Int = 2
    }

    private val createTable = """ CREATE TABLE 
            ${ConstantsTarefa.TAREFA.TABLE_NAME} (
            ${ConstantsTarefa.TAREFA.COLUNAS.ID} integer primary key autoincrement ,
            ${ConstantsTarefa.TAREFA.COLUNAS.COMPLETE} text ,
            ${ConstantsTarefa.TAREFA.COLUNAS.DESCRIPTION} text ,
            ${ConstantsTarefa.TAREFA.COLUNAS.DATE} text ,
            ${ConstantsTarefa.TAREFA.COLUNAS.HORA} text 

    );"""

    private val removeTable = "drop table if exists ${ConstantsTarefa.TAREFA.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTable)
        db.execSQL(createTable)
    }
}