package com.cody.androidexercise.ui.service

import android.content.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.*
import com.cody.androidexercise.service.MyService.MyBinder

class ServiceViewModel : ViewModel() {
    private val _isProgressBarUpdating = MutableLiveData<Boolean>()
    val isProgressBarUpdating: LiveData<Boolean> = _isProgressBarUpdating

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _maxValue = MutableLiveData(0)
    val maxValue: LiveData<Int> = _maxValue

    private val _binder = MutableLiveData<MyBinder>()
    val binder: LiveData<MyBinder> = _binder

    val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MyBinder
            _binder.postValue(binder)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            _binder.postValue(null)
        }
    }

    fun setIsProgressBarUpdating(isUpdating: Boolean) {
        _isProgressBarUpdating.value = isUpdating
    }

    fun setProgress(progress: Int) {
        _progress.value = progress
    }

    fun setMaxValue(value: Int) {
        _maxValue.value = value
    }
}