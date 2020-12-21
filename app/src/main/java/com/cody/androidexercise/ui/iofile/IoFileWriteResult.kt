package com.cody.androidexercise.ui.iofile

sealed class IoFileWriteResult {
    object Initial: IoFileWriteResult()
    object Ongoing: IoFileWriteResult()
    object Success: IoFileWriteResult()
    class Failure(val error: String): IoFileWriteResult()
}
