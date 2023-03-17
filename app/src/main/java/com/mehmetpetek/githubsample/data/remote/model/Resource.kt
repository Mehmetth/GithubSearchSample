package com.mehmetpetek.githubsample.data.remote.model

sealed class Resource<T>(val data: T? = null, val message: Throwable? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Fail<T>(data: T? = null) : Resource<T>(data, null)
    class Error<T>(message: Throwable? = null) : Resource<T>(null, message)
}