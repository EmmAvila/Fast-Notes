package com.emmavila.fastnotes.data.register.response


import com.google.gson.annotations.SerializedName


data class CorrectRegisterResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("expiresIn")
    val expiresIn: String,
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("localId")
    val localId: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)