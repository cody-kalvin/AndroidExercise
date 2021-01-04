package com.cody.androidexercise.data.facebook

import com.google.gson.annotations.SerializedName

data class FacebookMeResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)