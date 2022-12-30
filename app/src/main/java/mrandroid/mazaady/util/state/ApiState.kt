package mrandroid.mazaady.util.state

import okhttp3.ResponseBody

sealed class ApiState<T>(val data: T? = null, val message: UiText? = null) {
    class Loading<T> : ApiState<T>()
    class Success<T>(data: T?) : ApiState<T>(data = data)
    class TokenExpired<T> : ApiState<T>()
    class Error<T>(
        message: UiText,
        val errorCode: Int = -1,
        val errorBody: ResponseBody? = null
    ) : ApiState<T>(message = message)
}
