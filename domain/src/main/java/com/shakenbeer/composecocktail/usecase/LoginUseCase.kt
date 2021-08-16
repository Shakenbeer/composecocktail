package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.repository.LoginRepository

class LoginUseCase(private val loginRepository: LoginRepository) {
    fun execute(email: String, password: String): Result<Any> {
        return loginRepository.login(email, password)
    }
}