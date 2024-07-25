package com.emmavila.fastnotes.ui.note

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmavila.fastnotes.R
import com.emmavila.fastnotes.data.note.NoteRequest
import com.emmavila.fastnotes.data.note.NoteResponse
import com.emmavila.fastnotes.databinding.ActivityDetailBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityDetailBinding
    val gson = Gson()
    private var existingNote = NoteResponse()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val noteExtra = intent.getStringExtra("note")

        if (noteExtra != null) {
            existingNote = gson.fromJson(noteExtra, NoteResponse::class.java)
            mBinding.apply {
                etTitle.setText(existingNote.title)
                etDescription.setText(existingNote.description)
                tvActivityTitle.text = "Editar Nota"
            }

        }
        mBinding.ivSave.setOnClickListener {

            saveNote(existingNote.id)
        }

        mBinding.etTitle.requestFocus()


    }

    private fun saveNote(id: String? = null) {

        val title = mBinding.etTitle.text.toString()
        val description = mBinding.etDescription.text.toString()
        if (title.isBlank()) {
            mBinding.etTitle.error = "Titulo requerido"
            return
        } else {
            mBinding.etTitle.error = null
        }
        val note =
            NoteRequest(title = title, description = description, timestamp = Timestamp.now())
        saveToFireBase(note, id)
    }

    private fun saveToFireBase(noteRequest: NoteRequest, id: String?) {
        val collection: DocumentReference = if (id != null) {
            com.emmavila.fastnotes.core.Utils.getCollectionReference().document(id)
        } else {
            com.emmavila.fastnotes.core.Utils.getCollectionReference().document()
        }
        collection.set(noteRequest).addOnCompleteListener { saveTask ->
            if (saveTask.isSuccessful) {

                Toast.makeText(this, "Nota guardada con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {


//                Toast.makeText(this, "Algo malio sal", Toast.LENGTH_SHORT).show()
                val exception = saveTask.exception
                if (exception is FirebaseNetworkException) {
                    showInfoDialog("Revisa tu conexión a internet e intetalo de nuevo.")
                    return@addOnCompleteListener
                }

                if (exception is FirebaseFirestoreException) {
                    // Access the error code
                    val errorCode = exception.code

                    // Handle error based on the error code
                    when (errorCode) {
                        FirebaseFirestoreException.Code.NOT_FOUND -> {
                            // Handle email already in use error
                            showInfoDialog("No encontramos esta nota en la nube. Intentalo más tarde")
                        }
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                            // Handle invalid email error
                            showInfoDialog("Tu usuario no tiene permisos para guardar datos")
                        }

                        else -> {
                            // Handle other errors
                            showInfoDialog("Hubo un error desconocido, inténtalo más tarde")
                        }
                    }
                } else {
                    // Handle non-FirebaseException errors
                    showInfoDialog("Hubo un error desconocido, inténtalo más tarde")
                }
            }
        }
    }

    private fun showInfoDialog(message: String) {
        val infoDialog = com.emmavila.fastnotes.core.Utils.createInfoDialog(
            this,
            getString(R.string.ups),
            message
        )
        infoDialog.show()
    }
}