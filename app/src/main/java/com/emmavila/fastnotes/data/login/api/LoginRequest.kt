package com.emmavila.fastnotes.data.login.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("returnSecureToken")
    val returnToken: Boolean = true
)
