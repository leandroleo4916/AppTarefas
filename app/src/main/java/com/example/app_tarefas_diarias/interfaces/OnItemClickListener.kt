package com.example.app_tarefas_diarias.interfaces

interface OnItemClickListener{
    fun onEditClick(position: Int)
    fun onDeleteClick(position: Int)
    fun onCompleteClick(position: Int)
}