package com.cody.androidexercise.ui.iofile

sealed class IoFileLoadResult {
    object Initial: IoFileLoadResult()
    object Ongoing: IoFileLoadResult()
    class Success(val content: String): IoFileLoadResult()
    class Failure(val error: String): IoFileLoadResult()
}
