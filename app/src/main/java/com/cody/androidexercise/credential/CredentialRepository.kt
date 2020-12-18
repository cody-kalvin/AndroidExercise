package com.cody.androidexercise.credential

import android.content.*

object CredentialRepository {
    private const val FB_CREDENTIALS = "fb_credentials"

    private const val FB_ACCESS_TOKEN = "fb_access_token"

    private lateinit var instance: SharedPreferences

    fun init(context: Context) {
        instance = context.getSharedPreferences(FB_CREDENTIALS, Context.MODE_PRIVATE)
    }

    var facebookToken: String = ""
        set(value) {
            field = value
            val editor = instance.edit()
            editor.putString(FB_ACCESS_TOKEN, value)
            editor.apply()
        }
        get() {
            return instance.getString(FB_ACCESS_TOKEN, "") ?: ""
        }
}