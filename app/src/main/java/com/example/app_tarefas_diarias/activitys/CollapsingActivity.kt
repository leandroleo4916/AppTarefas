package com.example.app_tarefas_diarias.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_tarefas_diarias.R
import java.util.*

class CollapsingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)



        val MAX_ITEMS = 20
        val items: MutableList<String> = ArrayList()
        for (i in 1..MAX_ITEMS) {
            items.add("Item $i")
        }
        val recyclerView: RecyclerView = findViewById<RecyclerView>(android.R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ListAdapter(items)
    }


}