package com.example.marvel.ui.stats

sealed class ResourceStates<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : ResourceStates<T>(data)
    class Error<T>(message: String, data: T? = null) : ResourceStates<T>(data, message)
    class Loading<T> : ResourceStates<T>()
    class Empty<T> : ResourceStates<T>()
}