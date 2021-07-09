package com.example.app_tarefas_diarias.model

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.entity.EntityTarefa

class PointsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(tarefa: EntityTarefa){

        val box = itemView.findViewById<RadioButton>(R.id.radio_tarefa)
        val textDescription = itemView.findViewById<TextView>(R.id.text_nome_tarefa)
        val textDate = itemView.findViewById<TextView>(R.id.text_data_tarefa)
        val textHora = itemView.findViewById<TextView>(R.id.text_hora_tarefa)

        if (tarefa.complete == "1"){ box.isChecked = true }
        textDescription.text = tarefa.description
        textDate.text = tarefa.date
        textHora.text = tarefa.hora

    }
}