package com.cody.androidexercise.ui.assetdata

import android.content.Context
import androidx.lifecycle.*
import java.io.*

class AssetDataViewModel : ViewModel() {
    val content: MutableLiveData<String> = MutableLiveData("")

    private var _readResult: MutableLiveData<AssetDataReadResult> =
        MutableLiveData(AssetDataReadResult.Initial)
    val readResult: LiveData<AssetDataReadResult> = _readResult

    fun readFile(context: Context) {
        _readResult.value = AssetDataReadResult.Ongoing

        var reader: BufferedReader? = null

        try {
            val fileName = "assets.txt"
            val assets = context.assets
            val inputStream = assets.open(fileName)
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
            _readResult.value = AssetDataReadResult.Success(body)
            content.value = body
        } catch (e: IOException) {
            _readResult.value = AssetDataReadResult.Failure(e.message ?: "Unknown error")
        } finally {
            reader?.close()
        }
    }
}