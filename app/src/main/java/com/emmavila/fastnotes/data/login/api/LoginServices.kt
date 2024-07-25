package com.emmavila.fastnotes.data.login.api

import com.emmavila.fastnotes.data.login.response.BaseLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginServices {

    private val retrofit =
        com.emmavila.fastnotes.core.Utils.getRetrofit(com.emmavila.fastnotes.core.Constants.FIREBASE_BASE_URL)

    suspend fun loginUser(
        loginRequest: com.emmavila.fastnotes.data.login.api.LoginRequest,
        apiKey: String
    ): BaseLoginResponse {
        return withContext(Dispatchers.IO) {
            try {

                val response =
                    retrofit.create(com.emmavila.fastnotes.data.login.api.LoginInterface::class.java)
                        .loginUser(
                            loginRequest,
                            apiKey
                        )
                if (response.isSuccessful) {
                    BaseLoginResponse.Success(response.body())
                } else {
                    BaseLoginResponse.Error()
                }
            } catch (e: Throwable) {
                BaseLoginResponse.Error(com.emmavila.fastnotes.core.Utils.checkException(e))
            }


        }
    }
}