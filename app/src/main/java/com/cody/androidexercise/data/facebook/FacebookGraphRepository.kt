package com.cody.androidexercise.data.facebook

import android.os.Bundle
import com.facebook.*
import com.google.gson.Gson

class FacebookGraphRepository(private val accessToken: AccessToken) {

    fun fetchMe(callback: (me: FacebookMeResponse) -> Unit) {
        val request = GraphRequest.newMeRequest(accessToken) { _, response ->
            val me = Gson().fromJson(response.rawResponse, FacebookMeResponse::class.java)
            callback(me)
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name")
        request.parameters = parameters
        request.executeAsync()
    }
}