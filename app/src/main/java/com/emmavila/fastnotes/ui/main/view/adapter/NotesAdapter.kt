package com.emmavila.fastnotes.ui.main.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emmavila.fastnotes.R
import com.emmavila.fastnotes.data.note.NoteResponse

class NotesAdapter(
    private var noteList: MutableList<NoteResponse> = mutableListOf(),
    private val listener: OnItemClick
) :
    RecyclerView.Adapter<NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)

        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val item = noteList[position]
        holder.bind(item, position, listener)
    }

    override fun getItemCount(): Int = noteList.size

    fun updateList(noteList: MutableList<NoteResponse>) {
        this.noteList = noteList
        notifyDataSetChanged()

    }

    fun deleteNote(position: Int) {
        noteList.removeAt(position)
        notifyItemRemoved(position)
    }
}