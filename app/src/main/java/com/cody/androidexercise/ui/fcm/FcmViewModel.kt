package com.cody.androidexercise.ui.fcm

import androidx.lifecycle.*

class FcmViewModel : ViewModel() {
    private val _body = MutableLiveData<String>()
    val body: LiveData<String> = _body

    fun setBody(body: String?) {
        _body.value = body
    }
}