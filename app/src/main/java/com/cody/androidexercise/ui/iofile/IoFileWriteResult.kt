package com.cody.androidexercise.ui.iofile

sealed class IoFileWriteResult {
    object Initial: IoFileWriteResult()
    object Ongoing: IoFileWriteResult()
    class Success(val content: String): IoFileWriteResult()
    class Failure(val error: String): IoFileWriteResult()
}
