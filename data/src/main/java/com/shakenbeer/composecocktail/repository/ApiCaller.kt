package com.shakenbeer.composecocktail.repository

import com.google.gson.JsonParseException
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.connectivity.Connectivity
import okhttp3.ResponseBody
import retrofit2.Call
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> callApi(connectivity: Connectivity, call: Call<T>): Result<T> {
    if (connectivity.isConnectedToInternet()) {
        try {
            val response = call.execute()
            return if (response.isSuccessful) {
                response.body()?.let { Success(it) } ?: Error(
                    Error.Reason.API_ERROR, Throwable("Unknown API error")
                )

            } else {
                parseApiError(response.errorBody())
            }
        } catch (ex: Exception) {
            return if (ex is SocketTimeoutException || ex is UnknownHostException) {
                Error(Error.Reason.NO_INTERNET, ex)
            } else if (ex is JsonParseException) {
                Error(Error.Reason.API_MODEL_PARSING, ex)
            } else {
                Error(Error.Reason.UNKNOWN, ex)
            }
        }
    } else {
        return Error(Error.Reason.NO_INTERNET, Throwable("No internet connection"))
    }
}

@Suppress("UNUSED_PARAMETER")
private fun parseApiError(responseBody: ResponseBody?): Error {
    return Error(Error.Reason.API_ERROR, Throwable(responseBody?.string() ?: "Unknown API error"))
}