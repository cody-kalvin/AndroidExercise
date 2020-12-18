package com.cody.androidexercise.ui.facebook

import android.app.Application
import androidx.lifecycle.*
import com.cody.androidexercise.credential.CredentialRepository
import com.cody.androidexercise.data.facebook.FacebookGraphRepository
import com.facebook.AccessToken
import com.google.gson.Gson

class FacebookDataViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FacebookDataViewModel::class.java)) {
            val accessToken = Gson().fromJson(CredentialRepository.facebookToken, AccessToken::class.java)
            val repository = FacebookGraphRepository(accessToken)
            return FacebookDataViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}