package com.cody.androidexercise

import android.app.Application
import com.cody.androidexercise.util.SharedPrefUtil

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPrefUtil.init(this)
    }
}