package com.emmavila.fastnotes.ui.register.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmavila.fastnotes.data.register.response.RegisterBaseResponse
import com.emmavila.fastnotes.domain.register.RegisterUser
import kotlinx.coroutines.launch

class RegisterViewModel() : ViewModel() {
    val registerResult = MutableLiveData<RegisterBaseResponse>()
    private val registerUser = RegisterUser()

    fun register(email: String, password: String, apiKey: String) {

        registerResult.postValue(RegisterBaseResponse.Loading())

        viewModelScope.launch {

            val result = registerUser(email, password, apiKey)
            registerResult.postValue(result)


        }
    }


}