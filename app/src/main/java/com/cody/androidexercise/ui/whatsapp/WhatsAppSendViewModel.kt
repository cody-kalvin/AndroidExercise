package com.cody.androidexercise.ui.whatsapp

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.*

private const val WHATSAPP_PACKAGE = "com.whatsapp"

class WhatsAppSendViewModel : ViewModel() {
    val message: MutableLiveData<String> = MutableLiveData("")

    fun isPackageInstalled(packageManager: PackageManager): Boolean {
        try {
            packageManager.getPackageInfo(WHATSAPP_PACKAGE, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    fun sendIntent(): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message.value!!)
            `package` = WHATSAPP_PACKAGE
        }
        return intent
    }
}