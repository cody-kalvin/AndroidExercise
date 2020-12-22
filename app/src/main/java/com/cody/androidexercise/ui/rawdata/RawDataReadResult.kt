package com.cody.androidexercise.ui.rawdata

sealed class RawDataReadResult {
    object Initial: RawDataReadResult()
    object Ongoing: RawDataReadResult()
    class Success(val content: String): RawDataReadResult()
    class Failure(val error: String): RawDataReadResult()
}
