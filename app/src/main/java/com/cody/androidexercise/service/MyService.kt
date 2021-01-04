package com.cody.androidexercise.service

import android.app.Service
import android.content.Intent
import android.os.*

class MyService : Service() {
    private var handler = Handler(Looper.getMainLooper())

    var progress = 0

    var maxValue = 100

    var isPaused: Boolean = true

    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        val service: MyService = this@MyService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun resetTask() {
        progress = 0
        unPauseTask()
    }

    fun pauseTask() {
        isPaused = true
    }

    fun unPauseTask() {
        isPaused = false
        startTask()
    }

    private fun startTask() {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (progress >= maxValue || isPaused) {
                    handler.removeCallbacks(this)
                    pauseTask()
                } else {
                    progress++
                    handler.postDelayed(this, 100)
                }
            }
        }
        handler.postDelayed(runnable, 100)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }
}