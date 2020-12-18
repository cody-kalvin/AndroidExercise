package com.cody.androidexercise.ui.facebook

import android.app.Application
import androidx.lifecycle.*
import com.cody.androidexercise.data.facebook.FacebookGraphRepository

class FacebookDataViewModel(
    application: Application,
    private val repository: FacebookGraphRepository
) : AndroidViewModel(application) {

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> = _name

    fun fetchMe() {
        repository.fetchMe { me ->
            _name.value = me.name
        }
    }
}