package com.example.app_tarefas_diarias.constants

class ConstantsTarefa private constructor(){

    object TAREFA {
        const val TABLE_NAME = "tarefa"
        object COLUNAS {
            const val ID = "id"
            const val COMPLETE = "complete"
            const val DESCRIPTION = "description"
            const val DATE = "date"
            const val HORA = "hora"
        }
    }
}