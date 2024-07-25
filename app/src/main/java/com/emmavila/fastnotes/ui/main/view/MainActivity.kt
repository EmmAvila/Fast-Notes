package com.emmavila.fastnotes.ui.main.view

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmavila.fastnotes.R
import com.emmavila.fastnotes.data.note.NoteResponse
import com.emmavila.fastnotes.databinding.ActivityMainBinding
import com.emmavila.fastnotes.ui.login.view.LoginActivity
import com.emmavila.fastnotes.ui.main.view.adapter.NotesAdapter
import com.emmavila.fastnotes.ui.main.view.adapter.OnItemClick
import com.emmavila.fastnotes.ui.note.DetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), OnItemClick, SearchView.OnQueryTextListener {
    lateinit var mBinding: ActivityMainBinding
    lateinit var notesAdapter: NotesAdapter
    var noteList = mutableListOf<NoteResponse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.ivMore.setOnClickListener { v ->
            val popup = PopupMenu(this, v)
            popup.menuInflater.inflate(R.menu.main_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.log_out -> {
                        logOut()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
        setUpUI()
    }

    private fun logOut() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

    private fun setUpUI() {
        notesAdapter = NotesAdapter(listener = this)
        mBinding.rvNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        mBinding.fabAddNote.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        mBinding.svNotes.setOnQueryTextListener(this)


    }


    override fun onResume() {
        super.onResume()
        fetchData()
        Log.e("resume", "resume")

    }

    private fun fetchData() {
        val ref = com.emmavila.fastnotes.core.Utils.getCollectionReference()
        ref.get().addOnSuccessListener { result ->
            noteList = mutableListOf()
            for (document in result) {
                val note = document.toObject<NoteResponse>()
                Log.d(TAG, "${document.id} => ${document.data}")
                note.id = document.id
                noteList.add(note)
            }
            if (noteList.isEmpty()) {

                mBinding.llEmptyNotes.visibility = View.VISIBLE
            } else {
                mBinding.llEmptyNotes.visibility = View.GONE
                notesAdapter.updateList(noteList)
            }


        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    override fun onClickNote(note: NoteResponse, position: Int) {
        val gson = Gson()
        val intent = Intent(this, DetailActivity::class.java)
        val noteJson = gson.toJson(note)
        intent.putExtra("note", noteJson)
        startActivity(intent)

    }

    override fun onLongClickNote(note: NoteResponse, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Deseas eliminar esta nota?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            deleteNote(note.id ?: "-1", position)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun deleteNote(id: String, position: Int) {
        val ref = com.emmavila.fastnotes.core.Utils.getCollectionReference()
        ref.document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
            notesAdapter.deleteNote(position)
        }
            .addOnFailureListener {

                Toast.makeText(this, "Hubo un error al elimnar la nota", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()) {
            notesAdapter.updateList(noteList)
        } else {
            val filtered = noteList.filter { note ->
                note.title.lowercase().contains(newText.lowercase()) ||
                        note.description.lowercase().contains(newText.lowercase())
            }.toMutableList()
            notesAdapter.updateList(filtered)
        }

        return true
    }


}