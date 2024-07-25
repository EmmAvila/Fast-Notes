package com.emmavila.fastnotes.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmavila.fastnotes.data.login.response.BaseLoginResponse
import com.emmavila.fastnotes.domain.login.LoginUser
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val loginResponse = MutableLiveData<BaseLoginResponse>()
    private val login = LoginUser()
    fun loginUser(email: String, password: String, apiKey: String) {
        loginResponse.postValue(BaseLoginResponse.Loading())
        viewModelScope.launch {
            val loginRequest = com.emmavila.fastnotes.data.login.api.LoginRequest(email, password)
            val result = login(loginRequest, apiKey)
            loginResponse.postValue(result)
        }
    }
}