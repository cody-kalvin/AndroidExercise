package com.cody.androidexercise.ui.service

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cody.androidexercise.R
import kotlin.math.roundToInt

@BindingAdapter("progress", "maxValue")
fun TextView.progressText(progress: Double, maxValue: Double) {
    val rate = if (maxValue != 0.0) {
        progress / maxValue * 100.0
    } else {
        0.0
    }
    text = resources.getString(R.string.format_percentage, rate.roundToInt())
}

@BindingAdapter("progress", "maxValue", "isUpdating")
fun Button.progressText(progress: Double, maxValue: Double, isUpdating: Boolean) {
    val command =  when {
        isUpdating -> resources.getString(R.string.action_pause)
        progress == 0.0 -> resources.getString(R.string.action_play)
        progress == maxValue -> resources.getString(R.string.action_replay)
        else -> resources.getString(R.string.action_play)
    }
    text = command
}