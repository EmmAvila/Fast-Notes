package com.emmavila.fastnotes.data.login.response

sealed class BaseLoginResponse {
    data class Success(val data: LoginResponse?) : BaseLoginResponse()
    data class Loading(val nothing: Nothing? = null) : BaseLoginResponse()
    data class Error(val msg: String? = null) : BaseLoginResponse()
}