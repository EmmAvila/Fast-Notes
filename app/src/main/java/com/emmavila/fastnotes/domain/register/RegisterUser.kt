package com.emmavila.fastnotes.domain.register

import com.emmavila.fastnotes.data.register.RegisterRepository
import com.emmavila.fastnotes.data.register.response.RegisterBaseResponse

class RegisterUser {
    private val registerRepository = RegisterRepository()

    suspend operator fun invoke(
        email: String,
        password: String,
        apiKey: String
    ): RegisterBaseResponse {
        return registerRepository.registerUser(email, password, apiKey)
    }
}