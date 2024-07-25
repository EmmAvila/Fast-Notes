package com.emmavila.fastnotes.data.register.response

sealed class RegisterBaseResponse {
    data class Success(val data: CorrectRegisterResponse? = null) : RegisterBaseResponse()
    data class Loading(val nothing: Nothing? = null) : RegisterBaseResponse()
    data class Error(val msg: String? = null) : RegisterBaseResponse()
}