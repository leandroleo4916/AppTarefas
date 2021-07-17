package com.example.app_tarefas_diarias.activitys

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val mItems: List<String>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(l: OnItemClickListener?) {
        mItemClickListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_list_item_1, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = mItems[position]
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val mTextView: TextView = v.findViewById<View>(R.id.text1) as TextView
        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(v, adapterPosition)
            }
        }

        init {
            v.setOnClickListener(this)
        }
    }
}