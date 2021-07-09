package com.example.app_tarefas_diarias.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import com.example.app_tarefas_diarias.entity.EntityTarefa

@Suppress("UNREACHABLE_CODE")
class AdapterTarefa(private val application: Application) : RecyclerView.Adapter<PointsViewHolder>() {

    private var mListTarefa: ArrayList<EntityTarefa> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {

        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_tarefas, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        item.startAnimation(animation)

        return PointsViewHolder(item)
    }

    // Send to ViewHolder item of List
    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {

        val fullTarefa = mListTarefa[position]
        holder.bind(fullTarefa)
    }

    override fun getItemCount(): Int {
        return mListTarefa.count()
    }

    fun udateTarefas(list: ArrayList<EntityTarefa>){
        mListTarefa = list.reversed() as ArrayList<EntityTarefa>
        notifyDataSetChanged()
    }
}