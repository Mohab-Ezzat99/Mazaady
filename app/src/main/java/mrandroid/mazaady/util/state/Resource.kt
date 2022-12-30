package mrandroid.mazaady.util.state

import okhttp3.ResponseBody

sealed class Resource<T>(val data: T? = null, val message: UiText? = null) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data)
    class TokenExpired<T> : Resource<T>()
    class Error<T>(
        message: UiText,
        data: T? = null,
        val errorCode: Int = -1,
        val errorBody: ResponseBody? = null
    ) : Resource<T>(data, message)
}
