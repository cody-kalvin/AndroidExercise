package com.cody.androidexercise.ui.facebook

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.cody.androidexercise.util.SharedPrefUtil
import com.facebook.*
import com.facebook.login.*

class FacebookLoginViewModel : ViewModel() {
    private lateinit var callbackManager: CallbackManager

    fun initLoginCallback() {
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken
                    if (accessToken != null && !accessToken.isExpired) {
                        SharedPrefUtil.facebookToken = accessToken.token
                    }
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException?) {}
            }
        )
    }

    fun onLoginResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}