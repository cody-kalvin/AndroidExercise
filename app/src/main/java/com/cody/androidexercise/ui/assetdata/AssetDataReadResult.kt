package com.cody.androidexercise.ui.assetdata

sealed class AssetDataReadResult {
    object Initial: AssetDataReadResult()
    object Ongoing: AssetDataReadResult()
    class Success(val content: String): AssetDataReadResult()
    class Failure(val error: String): AssetDataReadResult()
}
