package com.emmavila.fastnotes.data.login.api

import com.emmavila.fastnotes.data.login.response.LoginResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginInterface {
    @POST("v1/accounts:signInWithPassword")
    suspend fun loginUser(
        @Body loginRequest: com.emmavila.fastnotes.data.login.api.LoginRequest,
        @Query("key") key: String
    ): Response<LoginResponse>
}