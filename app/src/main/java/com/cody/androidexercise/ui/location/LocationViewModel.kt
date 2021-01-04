package com.cody.androidexercise.ui.location

import android.content.*
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.*
import com.cody.androidexercise.service.ForegroundLocationService

class LocationViewModel : ViewModel() {
    private val _isTrackingEnabled = MutableLiveData<Boolean>()
    val isTrackingEnabled: LiveData<Boolean> = _isTrackingEnabled

    private val _isPermissionGranted = MutableLiveData<Boolean>()
    val isPermissionGranted: LiveData<Boolean> = _isPermissionGranted

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation

    private val _binder = MutableLiveData<ForegroundLocationService.MyBinder>()
    val binder: LiveData<ForegroundLocationService.MyBinder> = _binder

    fun setTrackingEnabled(boolean: Boolean) {
        _isTrackingEnabled.value = boolean
    }

    fun setPermissionGranted(boolean: Boolean) {
        _isPermissionGranted.value = boolean
    }

    fun setCurrentLocation(location: Location?) {
        _currentLocation.value = location
    }

    val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.MyBinder
            _binder.postValue(binder)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            _binder.postValue(null)
        }
    }
}