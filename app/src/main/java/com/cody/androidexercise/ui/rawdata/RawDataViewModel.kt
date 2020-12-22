package com.cody.androidexercise.ui.rawdata

import android.content.Context
import androidx.lifecycle.*
import com.cody.androidexercise.R
import java.io.*
import java.lang.StringBuilder

class RawDataViewModel: ViewModel() {
    val content: MutableLiveData<String> = MutableLiveData("")

    private val _readResult: MutableLiveData<RawDataReadResult> = MutableLiveData(RawDataReadResult.Initial)
    val readResult: LiveData<RawDataReadResult> = _readResult

    fun readFile(context: Context) {
        _readResult.value = RawDataReadResult.Ongoing

        var reader: BufferedReader? = null

        try {
            val inputStream = context.resources.openRawResource(R.raw.rawfiles)
            reader = BufferedReader(InputStreamReader(inputStream))
            val builder = StringBuilder()
            var str: String?
            do {
                str = reader.readLine()
                str?.let {
                    builder.append(it + "\n")
                }
            } while (str != null)

            val body = builder.toString()
            _readResult.value = RawDataReadResult.Success(body)
            content.value = body
        } catch (e: IOException) {
            _readResult.value = RawDataReadResult.Failure(e.message ?: "Unknown error")
        } finally {
            reader?.close()
        }
    }
}