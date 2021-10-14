package com.shakenbeer.composecocktail

sealed class  Result<out T>

class Success<out T>(val value: T) : Result<T>()

class Error(
    val reason: Reason = Reason.UNKNOWN,
    val throwable: Throwable
) : Result<Nothing>() {
    enum class Reason {
        NO_INTERNET,
        API_ERROR,
        INVALID_CREDS,
        API_MODEL_PARSING,
        UNKNOWN
    }
}