package com.cody.androidexercise.ui.iofile

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("writeResult")
fun ProgressBar.setWriteResult(result: IoFileWriteResult) {
    visibility = when (result) {
        IoFileWriteResult.Initial -> View.GONE
        IoFileWriteResult.Ongoing -> View.VISIBLE
        is IoFileWriteResult.Success -> View.GONE
        is IoFileWriteResult.Failure -> View.GONE
    }
}

@BindingAdapter("loadResult")
fun ProgressBar.setLoadResult(result: IoFileLoadResult) {
    visibility = when (result) {
        IoFileLoadResult.Initial -> View.GONE
        IoFileLoadResult.Ongoing -> View.VISIBLE
        is IoFileLoadResult.Success -> View.GONE
        is IoFileLoadResult.Failure -> View.GONE
    }
}