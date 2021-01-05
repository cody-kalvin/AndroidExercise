package com.cody.androidexercise.ui.location

import android.location.Location
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cody.androidexercise.R

@BindingAdapter("location")
fun TextView.setLocation(location: Location?) {
    text = if (location != null) {
        resources.getString(R.string.display_coordinates, location.latitude, location.longitude)
    } else {
        resources.getString(R.string.message_no_location_tracking)
    }
}

@BindingAdapter("trackingEnabled", "permissionGranted")
fun Button.setPermissionRequest(isTrackingEnabled: Boolean?, isPermissionGranted: Boolean?) {
    text = when {
        isPermissionGranted == true && isTrackingEnabled == true -> resources.getString(R.string.action_pause)
        isPermissionGranted == true -> resources.getString(R.string.action_start)
        else -> resources.getString(R.string.action_request_permission)
    }
}