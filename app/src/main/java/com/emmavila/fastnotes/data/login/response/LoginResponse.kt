package com.emmavila.fastnotes.data.login.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("displayName")
    val displayName: String,
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
    val refreshToken: String,
    @SerializedName("registered")
    val registered: Boolean
)