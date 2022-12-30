package mrandroid.mazaady.util.state

import okhttp3.ResponseBody

sealed class UiState<T>(val data: T? = null, val message: UiText? = null) {
    class Loading<T> : UiState<T>()
    class Success<T>(data: T?) : UiState<T>(data)
    class TokenExpired<T> : UiState<T>()
    class Empty<T> : UiState<T>()
    class Error<T>(
        message: UiText,
        data: T? = null,
        val errorCode: Int = -1,
        val errorBody: ResponseBody? = null
    ) : UiState<T>(data, message)
}
