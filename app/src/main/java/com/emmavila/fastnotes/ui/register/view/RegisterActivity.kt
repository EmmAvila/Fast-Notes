package com.emmavila.fastnotes.ui.register.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.emmavila.fastnotes.R
import com.emmavila.fastnotes.core.Utils
import com.emmavila.fastnotes.data.register.response.RegisterBaseResponse
import com.emmavila.fastnotes.databinding.ActivityRegisterBinding
import com.emmavila.fastnotes.ui.login.view.LoginActivity
import com.emmavila.fastnotes.ui.register.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException


class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initObservers()
        setUpUi()
    }

    private fun initObservers() {
        registerViewModel.registerResult.observe(this) {
            when (it) {
                is RegisterBaseResponse.Error -> {
                    if (it.msg != null) {
                        normalUi()
                        showInfoDialog(it.msg)
                    }
                }
                is RegisterBaseResponse.Success -> {
                    val successData = it.data
                    if (successData != null) {
                        normalUi()
                        successfulRegistration()
                    }
                }
                is RegisterBaseResponse.Loading -> {
                    waitingU()
                }
                // Add more cases for other response types if needed
                else -> {}
            }
        }

    }

    private fun waitingU() {
        with(mBinding) {
            btnRegister.apply {
                text = getString(R.string.empty)
                isEnabled = false
                isClickable = false
            }
            progress.visibility = View.VISIBLE
        }
    }

    private fun normalUi() {
        with(mBinding) {
            btnRegister.apply {
                text = getString(R.string.register_button)
                isEnabled = true
                isClickable = true
            }
            progress.visibility = View.GONE
        }

    }


    private fun successfulRegistration() {
        Toast.makeText(this, getString(R.string.successfulRegistration), Toast.LENGTH_LONG).show()
        navigateToLogin()
    }

    private fun showInfoDialog(message: String) {
        val infoDialog = com.emmavila.fastnotes.core.Utils.createInfoDialog(
            this@RegisterActivity,
            getString(R.string.ups),
            message
        )
        infoDialog.show()
    }

    private fun setUpUi() {
        mBinding.tvAccount.setOnClickListener {
            navigateToLogin()
        }
        mBinding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    private fun registerUser() {
        val user = mBinding.etUser.text.toString()
        val password = mBinding.etPass.text.toString()
        val apiKey = getString(R.string.firebase_api_key)

        if (validateFields(user, password)) {

            createAccount(user, password)
//            registerViewModel.register(user, password, apiKey)
        } else {
            Toast.makeText(
                this@RegisterActivity,
                "Llena los campos requeridos",
                Toast.LENGTH_SHORT
            ).show()
//            showInfoDialog("Debes ingresar un correo valido y una contraseña de al menos 6 caracteres")
        }


    }


    fun createAccount(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.setLanguageCode("es")

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    // User registration was successful
                    firebaseAuth.signOut()
                    successfulRegistration()
                } else {
                    // User registration failed
                    val exception = result.exception

                    if (exception is FirebaseAuthException) {
                        // Access the error code
                        val errorCode = exception.errorCode

                        // Handle error based on the error code
                        when (errorCode) {
                            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                // Handle email already in use error
                                showInfoDialog("Email no disponible.")
                            }
                            "ERROR_INVALID_EMAIL" -> {
                                // Handle invalid email error
                                showInfoDialog("Este correo no es valido.")
                            }
                            "ERROR_OPERATION_NOT_ALLOWED" -> {
                                // Handle operation not allowed error
                                showInfoDialog("Operacion no permitida.")
                            }
                            "ERROR_WEAK_PASSWORD" -> {
                                // Handle weak password error
                                showInfoDialog("La contraseña es muy debil.")
                            }
                            else -> {
                                // Handle other errors
                                showInfoDialog("Registro fallido. Intenta de nuevo")
                            }
                        }
                    } else {
                        // Handle non-FirebaseException errors
                        showInfoDialog("Registro fallido. Intenta de nuevo")
                    }
                }
            }
    }


    private fun validateFields(user: String, password: String): Boolean {
        val validEmail = Utils.validEmail(user)
        val validPassword = password.length >= 6

        if (!validEmail) {
            mBinding.tilUser.error = "Ingresa email válido"
        } else {
            mBinding.tilUser.error = null
        }

        if (!validPassword) {
            mBinding.tilPass.error = "Mínimo 6 caracteres"
        } else {
            mBinding.tilPass.error = null
        }
        return validEmail && validPassword
    }
}