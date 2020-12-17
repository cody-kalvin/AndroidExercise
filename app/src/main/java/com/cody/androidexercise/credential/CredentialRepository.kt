package com.cody.androidexercise.credential

import android.content.*
import com.cody.androidexercise.R

object CredentialRepository {
    private lateinit var context: Context

    private lateinit var instance: SharedPreferences

    fun init(context: Context) {
        this.context = context

        this.instance = context.getSharedPreferences(
            context.resources.getString(R.string.fb_credential),
            Context.MODE_PRIVATE
        )
    }

    var facebookToken: String = ""
        set(value) {
            field = value
            val editor = instance.edit()
            editor.putString(context.resources.getString(R.string.fb_access_token), value)
            editor.apply()
        }
        get() {
            val key = context.resources.getString(R.string.fb_access_token)
            return instance.getString(key, "") ?: ""
        }
}