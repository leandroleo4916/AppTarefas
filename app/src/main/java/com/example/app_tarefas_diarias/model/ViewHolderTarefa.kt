package com.example.app_tarefas_diarias.model

import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.entity.EntityTarefa

class ViewHolderTaref(itemView: View, tarefa: EntityTarefa) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val option = itemView.findViewById<ImageView>(R.id.edit_tarefa)
    val box = itemView.findViewById<RadioButton>(R.id.radio_tarefa)
    val textDescription = itemView.findViewById<TextView>(R.id.text_nome_tarefa)
    val textDate = itemView.findViewById<TextView>(R.id.text_data_tarefa)
    val textHora = itemView.findViewById<TextView>(R.id.text_hora_tarefa)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}