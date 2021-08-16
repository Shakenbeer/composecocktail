package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Login

interface LoginRepository {
    fun login(email: String, password: String): Result<Login>
}