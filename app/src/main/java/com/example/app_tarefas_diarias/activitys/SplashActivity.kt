package com.example.app_tarefas_diarias.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.app_tarefas_diarias.R

class SplashActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val item = findViewById<ImageView>(R.id.image_splash)
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.show)
        item.startAnimation(animation)

        val handle = Handler()
        handle.postDelayed({ showTaskActivity() }, 2000)
    }

    private fun showTaskActivity() {
        val intent = Intent(this@SplashActivity, ActivityTask::class.java)
        startActivity(intent)
        finish()
    }
}