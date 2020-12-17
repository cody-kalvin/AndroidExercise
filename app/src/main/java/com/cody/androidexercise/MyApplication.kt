package com.cody.androidexercise

import android.app.Application
import com.cody.androidexercise.credential.CredentialRepository

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CredentialRepository.init(this)
    }
}