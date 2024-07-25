package com.emmavila.fastnotes.data.register.response

data class ErrorRegisterResponse(
    val error: Error = Error()
)

data class Error(
    val code: Int = 0,
    val message: String = "",
    val status: String = ""
)

