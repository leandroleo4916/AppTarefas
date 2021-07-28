package com.example.app_tarefas_diarias.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.app_tarefas_diarias.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handle = Handler()
        handle.postDelayed(Runnable { showTarefaActivity() }, 2000)
    }

    private fun showTarefaActivity() {
        val intent = Intent(this@SplashActivity, ActivityTarefa::class.java)
        startActivity(intent)
        finish()
    }
}