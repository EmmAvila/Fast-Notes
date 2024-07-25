package com.emmavila.fastnotes.ui.main.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.emmavila.fastnotes.data.note.NoteResponse
import com.emmavila.fastnotes.databinding.ItemNoteBinding

class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemNoteBinding.bind(view)
    fun bind(item: NoteResponse, position: Int, listener: OnItemClick) {
        binding.tvTitleNote.text = item.title
        binding.tvDescriptionNote.text = item.description
        binding.root.setOnClickListener {
            listener.onClickNote(item, position)
        }
        binding.root.setOnLongClickListener {
            listener.onLongClickNote(item, position)
            true
        }
    }
}