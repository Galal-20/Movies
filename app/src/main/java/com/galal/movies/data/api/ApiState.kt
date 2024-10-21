package com.galal.movies.data.api


sealed class ApiState<out T> {
    data class Success<T>(val data: T) : ApiState<T>()
    data class Failure(val message: String) : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
}
