package com.emmavila.fastnotes.domain.login

import com.emmavila.fastnotes.data.login.response.BaseLoginResponse

class LoginUser {
    private val loginRepository = com.emmavila.fastnotes.data.login.LoginRepository()

    suspend operator fun invoke(
        loginRequest: com.emmavila.fastnotes.data.login.api.LoginRequest,
        apiKey: String
    ): BaseLoginResponse {
        return loginRepository.login(loginRequest, apiKey)
    }
}