package com.emmavila.fastnotes.data.register

import com.emmavila.fastnotes.data.register.api.RegisterServices
import com.emmavila.fastnotes.data.register.response.RegisterBaseResponse

class RegisterRepository {
    private val registerServices = RegisterServices()

    suspend fun registerUser(
        email: String,
        password: String,
        apiKey: String
    ): RegisterBaseResponse {
        return registerServices.registerUser(email, password, apiKey)
    }
}