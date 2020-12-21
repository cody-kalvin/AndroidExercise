package com.cody.androidexercise.ui.iofile

import android.content.Context
import androidx.lifecycle.*
import java.io.*

class IoFileViewModel : ViewModel() {
    val content: MutableLiveData<String> = MutableLiveData("")

    private val _writeResult: MutableLiveData<IoFileWriteResult> =
        MutableLiveData(IoFileWriteResult.Initial)
    val writeResult: LiveData<IoFileWriteResult> = _writeResult

    private val _loadResult: MutableLiveData<IoFileLoadResult> =
        MutableLiveData(IoFileLoadResult.Initial)
    val loadResult: LiveData<IoFileLoadResult> = _loadResult

    fun saveFile(context: Context) {
        _writeResult.value = IoFileWriteResult.Ongoing

        var outputStream: OutputStream? = null

        try {
            val fileName = "sample.txt"
            val fileBody = content.value!!
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream?.let {
                it.write(fileBody.toByteArray())
                _writeResult.value = IoFileWriteResult.Success(fileBody)
            }
            content.value = ""
        } catch (e: FileNotFoundException) {
            _writeResult.value = IoFileWriteResult.Failure(e.message ?: "Unknown error")
        } catch (e: IOException) {
            _writeResult.value = IoFileWriteResult.Failure(e.message ?: "Unknown error")
        } finally {
            outputStream?.close()
        }
    }

    fun loadFile(context: Context) {
        _loadResult.value = IoFileLoadResult.Ongoing

        var reader: BufferedReader? = null

        try {
            val fileName = "sample.txt"
            val inputStream = context.openFileInput(fileName)
            reader = BufferedReader(InputStreamReader(inputStream))
            val line = reader.readLine()
            _loadResult.value = IoFileLoadResult.Success(line)
            content.value = line
        } catch (e: FileNotFoundException) {
            _loadResult.value = IoFileLoadResult.Failure(e.message ?: "Unknown error")
        } catch (e: IOException) {
            _loadResult.value = IoFileLoadResult.Failure(e.message ?: "Unknown error")
        } finally {
            reader?.close()
        }
    }
}