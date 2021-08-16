package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Const
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.connectivity.Connectivity
import com.shakenbeer.composecocktail.entity.Login

class LoginRemoteRepository(private val connectivity: Connectivity) : LoginRepository {

    override fun login(email: String, password: String): Result<Login> {
        Thread.sleep(2000)
        return if (connectivity.isConnectedToInternet()) {
            if (email == Const.EMAIL && password == Const.PASSWORD) {
                Success(Login("1"))
            } else {
                Error(Error.Reason.INVALID_CREDS, Throwable("Invalid credentials"))
            }
        } else {
            Error(Error.Reason.NO_INTERNET, Throwable("No internet connection"))
        }
    }
}