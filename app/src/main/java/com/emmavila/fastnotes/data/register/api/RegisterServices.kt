package com.emmavila.fastnotes.data.register.api

import com.emmavila.fastnotes.BuildConfig
import com.emmavila.fastnotes.data.register.response.ErrorRegisterResponse
import com.emmavila.fastnotes.data.register.response.RegisterBaseResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterServices {
    private val retrofit =
        com.emmavila.fastnotes.core.Utils.getRetrofit(com.emmavila.fastnotes.core.Constants.FIREBASE_BASE_URL)

    suspend fun registerUser(
        email: String,
        password: String,
        apiKey: String
    ): RegisterBaseResponse {
        return withContext(Dispatchers.IO) {
            try {
                val apiKeyi = BuildConfig.API_KEY
                val requestBody = RegisterRequest(email, password)
                val response = retrofit.create(RegisterInterface::class.java).registerUser(
                    requestBody,
                    apiKey
                )
                val gson = Gson()

                if (response.isSuccessful) {
                    RegisterBaseResponse.Success(response.body())
                } else {

                    val errorResponse = response.errorBody()?.string()

                    val jsonObject = gson.fromJson(errorResponse, ErrorRegisterResponse::class.java)
                    RegisterBaseResponse.Error(jsonObject.error.message)
                }
            } catch (e: Throwable) {
                RegisterBaseResponse.Error(com.emmavila.fastnotes.core.Utils.checkException(e))
            }


        }
    }
}