package com.emmavila.fastnotes.data.register.api

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("returnSecureToken")
    val returnToken: Boolean = true

)