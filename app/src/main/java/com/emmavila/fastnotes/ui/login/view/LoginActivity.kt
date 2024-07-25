package com.emmavila.fastnotes.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.emmavila.fastnotes.BuildConfig
import com.emmavila.fastnotes.R
import com.emmavila.fastnotes.core.Utils
import com.emmavila.fastnotes.data.login.response.BaseLoginResponse
import com.emmavila.fastnotes.data.login.response.LoginResponse
import com.emmavila.fastnotes.databinding.ActivityLoginBinding
import com.emmavila.fastnotes.ui.login.viewModel.LoginViewModel
import com.emmavila.fastnotes.ui.main.view.MainActivity
import com.emmavila.fastnotes.ui.register.view.RegisterActivity
import com.emmavila.fastnotes.ui.webPage.WebPageActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        checkLogged()
        setUpUi()
        initObservers()

    }

    private fun checkLogged() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initObservers() {
        loginViewModel.loginResponse.observe(this) {
            when (it) {
                is BaseLoginResponse.Loading -> {
                    waitingUi()
                }
                is BaseLoginResponse.Success -> {
                    normalUi()
                    it.data?.let { loginResponse -> processLogin(loginResponse) }
                }
                is BaseLoginResponse.Error -> {
                    normalUi()
                    showInfoDialog(getString(R.string.login_error))
                }
                else -> {
                    normalUi()
                }
            }

        }

    }

    private fun processLogin(data: LoginResponse?) {
        navigateToMain()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setUpUi() {
        with(mBinding) {
            tvNewAccount.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btnLogin.setOnClickListener {
//                val user = "e.avila.casta@gmail.com"
//                val password = "123456"
                val user = etUser.text.toString()
                val password = etPass.text.toString()
                val apiKey = getString(R.string.firebase_api_key)
                val api = BuildConfig.API_KEY
//                loginViewModel.loginUser(user, password, apiKey)

                if (validateFields(user, password)) {

                    loginFireBase(user, password)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Llena los campos requeridos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            tvDeleteAccount.setOnClickListener {
                openEliminateAccountWeb()
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

    private fun openEliminateAccountWeb() {
        val intent = Intent(this, WebPageActivity::class.java)
        startActivity(intent)
    }

    private fun loginFireBase(user: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                // User registration was successful
                println(result.result)
                processLogin(null)
            } else {
                // User registration failed
                val exception = result.exception
                if (exception is FirebaseNetworkException) {
                    showInfoDialog("Revisa tu conexión a internet e intetalo de nuevo.")
                    return@addOnCompleteListener
                }

                if (exception is FirebaseAuthException) {
                    // Access the error code
                    val errorCode = exception.errorCode

                    // Handle error based on the error code
                    when (errorCode) {
                        "ERROR_USER_NOT_FOUND" -> {
                            // Handle email already in use error
                            showInfoDialog("Usuario no registrado")
                        }
                        "ERROR_WRONG_PASSWORD" -> {
                            // Handle invalid email error
                            showInfoDialog("Tu contraseña no es correcta")
                        }
                        "ERROR_USER_DISABLED" -> {
                            // Handle operation not allowed error
                            showInfoDialog("Tu usuario ha sido deshabilitado. Contacta con soporte.")
                        }
                        else -> {
                            // Handle other errors
                            showInfoDialog("No pudimos iniciar sesion. Intentalo más tarde.")
                        }
                    }
                } else {
                    // Handle non-FirebaseException errors
                    showInfoDialog("No pudimos iniciar sesion. Intentalo más tarde.")
                }
            }
        }
    }

    private fun showInfoDialog(message: String) {
        val infoDialog = com.emmavila.fastnotes.core.Utils.createInfoDialog(
            this@LoginActivity,
            getString(R.string.ups),
            message
        )
        infoDialog.show()
    }

    private fun waitingUi() {
        with(mBinding) {
            btnLogin.apply {
                text = getString(R.string.empty)
                isEnabled = false
                isClickable = false
            }
            progress.visibility = View.VISIBLE
        }
    }

    private fun normalUi() {
        with(mBinding) {
            btnLogin.apply {
                text = getString(R.string.login_button)
                isEnabled = true
                isClickable = true
            }
            progress.visibility = View.GONE
        }

    }
}
