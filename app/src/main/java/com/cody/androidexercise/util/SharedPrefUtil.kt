package com.cody.androidexercise.util

import android.content.*

object SharedPrefUtil {
    const val FB_CREDENTIALS = "fb_credentials"

    const val FB_ACCESS_TOKEN = "fb_access_token"

    const val FOREGROUND_LOCATION_TRACKING = "foreground_location_tracking"

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

    var locationTracking: Boolean = false
        set(value) {
            field = value
            val editor = instance.edit()
            editor.putBoolean(FOREGROUND_LOCATION_TRACKING, value)
            editor.apply()
        }
        get() {
            return instance.getBoolean(FOREGROUND_LOCATION_TRACKING, false)
        }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        instance.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        instance.unregisterOnSharedPreferenceChangeListener(listener)
    }
}