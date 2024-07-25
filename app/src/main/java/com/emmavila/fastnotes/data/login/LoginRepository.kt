package com.emmavila.fastnotes.data.login

import com.emmavila.fastnotes.data.login.response.BaseLoginResponse

class LoginRepository {
    private val loginServices = com.emmavila.fastnotes.data.login.api.LoginServices()

    suspend fun login(
        loginRequest: com.emmavila.fastnotes.data.login.api.LoginRequest,
        apiKey: String
    ): BaseLoginResponse {
        return loginServices.loginUser(loginRequest, apiKey)
    }
}