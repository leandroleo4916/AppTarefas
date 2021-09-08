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
import com.example.app_tarefas_diarias.entity.EntityTask
import kotlinx.android.synthetic.main.recycler_tarefas.view.*

class AdapterTask(private val application: Application, private val listener: OnItemClickListener
                    ): RecyclerView.Adapter<AdapterTask.ViewHolderTask>() {

    private var mListTask: ArrayList<EntityTask> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {

        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_tarefas, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.show)
        item.startAnimation(animation)

        return ViewHolderTask(item)
    }

    override fun onBindViewHolder(holderTask: ViewHolderTask, position: Int) {

        val fullTask = mListTask[position]
        holderTask.bind(fullTask)
    }

    override fun getItemCount(): Int {
        return mListTask.count()
    }

    inner class ViewHolderTask(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.edit_tarefa.setOnClickListener(this)
            itemView.delete_tarefa.setOnClickListener(this)
            itemView.toolbar_tarefas.setOnClickListener(this)
            itemView.complete_tarefa.setOnClickListener(this)
        }

        fun bind(task: EntityTask){

            itemView.text_nome_tarefa.text = task.description
            itemView.text_data_tarefa.text = task.date
            itemView.text_hora_tarefa.text = task.hora

            if (task.complete == "0") {
                itemView.complete_tarefa.setImageResource(R.drawable.ic_imcompleto)
                itemView.complete_tarefa.tag = 0
            }
            else {
                itemView.complete_tarefa.setImageResource(R.drawable.ic_completo)
                itemView.complete_tarefa.tag = 1
            }
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when (view) {
                itemView.edit_tarefa -> listener.onEditClick(position)
                itemView.delete_tarefa -> listener.onDeleteClick(position)
                itemView.complete_tarefa -> listener.onCompleteClick(position)
            }
        }
    }

    fun updateTasks(list: ArrayList<EntityTask>){
        if (list.size > 1) {
            mListTask = list.reversed() as ArrayList<EntityTask>
            notifyDataSetChanged()
        }else {
            mListTask = list
            notifyDataSetChanged()
        }
    }
}