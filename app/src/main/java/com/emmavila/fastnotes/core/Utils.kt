package com.emmavila.fastnotes.core

import android.app.AlertDialog
import android.content.Context
import android.util.Patterns
import com.emmavila.fastnotes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.regex.Pattern

object Utils {
    fun createInfoDialog(context: Context, title: String, message: String): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        val positiveButton = context.getString(R.string.accept)
        builder.setPositiveButton(positiveButton) { dialog, _ ->
            dialog.dismiss()

        }

        return builder.create()
    }

    fun checkException(e: Throwable): String {
        return when (e) {
            is IOException -> "Revisa tu conexión a internet e intentalo de nuevo."
            else -> "Sucedio un error inesperado. Estamos trabajando para solucionar el problema los mas pronto posible"
        }
    }

    fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getCollectionReference(): CollectionReference {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return FirebaseFirestore.getInstance().collection("notes")
            .document(currentUser?.uid ?: "Unknow").collection("my_notes")

    }

    fun validEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}