package mrandroid.mazaady.util

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mrandroid.mazaady.util.Constants.TAG
import mrandroid.mazaady.util.state.ApiState
import mrandroid.mazaady.util.state.UiText
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun <T> toResultFlow(call: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
    emit(ApiState.Loading())
    try {
        val response = call()
        if (response.isSuccessful) emit(ApiState.Success(response.body()))
        else {
            Log.d(TAG, response.message())
            emit(ApiState.Error(
                    UiText.DynamicString(response.message()),
                    response.raw().code(),
                    response.errorBody()
                )
            )
        }
    } catch (e: HttpException) {
        Log.d(TAG, e.stackTraceToString())
        emit(ApiState.Error(UiText.DynamicString("Something wrong")))
    } catch (e: IOException) {
        Log.d(TAG, e.stackTraceToString())
        emit(ApiState.Error(UiText.DynamicString("Check your internet connection")))
    } catch (e: Exception) {
        Log.d(TAG, e.stackTraceToString())
        emit(ApiState.Error(UiText.DynamicString("Something wrong")))
    }
}