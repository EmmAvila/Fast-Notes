package com.emmavila.fastnotes.data.register.api

import com.emmavila.fastnotes.data.register.response.CorrectRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RegisterInterface {
    @POST("v1/accounts:signUp")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest,
        @Query("key") key: String
    ): Response<CorrectRegisterResponse>
}