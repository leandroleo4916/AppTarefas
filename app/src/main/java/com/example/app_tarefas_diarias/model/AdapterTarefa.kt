package com.example.app_tarefas_diarias.model

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.interfaces.OnItemClickListener
import com.example.app_tarefas_diarias.entity.EntityTarefa
import kotlinx.android.synthetic.main.recycler_tarefas.view.*

@Suppress("UNREACHABLE_CODE")
class AdapterTarefa(private val application: Application, private val listener: OnItemClickListener
                    ): RecyclerView.Adapter<AdapterTarefa.ViewHolderTarefa>() {

    private var mListTarefa: ArrayList<EntityTarefa> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTarefa {

        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_tarefas, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom_in)
        item.startAnimation(animation)

        return ViewHolderTarefa(item)
    }

    override fun onBindViewHolder(holderTarefa: ViewHolderTarefa, position: Int) {

        val fullTarefa = mListTarefa[position]
        holderTarefa.bind(fullTarefa)
    }

    override fun getItemCount(): Int {
        return mListTarefa.count()
    }

    inner class ViewHolderTarefa(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.edit_tarefa.setOnClickListener(this)
            itemView.delete_tarefa.setOnClickListener(this)
            itemView.toolbar_tarefas.setOnClickListener(this)
            itemView.complete_tarefa.setOnClickListener(this)
        }

        fun bind(tarefa: EntityTarefa){
            if (tarefa.complete == "0") {
                itemView.complete_tarefa.setImageResource(R.drawable.ic_todo)
                itemView.complete_tarefa.tag = 0
            }
            else {
                itemView.complete_tarefa.setImageResource(R.drawable.ic_completo)
                itemView.complete_tarefa.tag = 1
            }
            itemView.text_nome_tarefa.text = tarefa.description
            itemView.text_data_tarefa.text = tarefa.date
            itemView.text_hora_tarefa.text = tarefa.hora
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            when (v) {
                itemView.edit_tarefa -> listener.onEditClick(position)
                itemView.delete_tarefa -> listener.onDeleteClick(position)
                itemView.complete_tarefa -> listener.onCompleteClick(position)
            }
        }
    }

    fun updateTarefas(list: ArrayList<EntityTarefa>){
        if (list.size > 1) {
            mListTarefa = list.reversed() as ArrayList<EntityTarefa>
            notifyDataSetChanged()
        }else {
            mListTarefa = list
            notifyDataSetChanged()
        }
    }
}