package com.emmavila.fastnotes.ui.main.view.adapter

import com.emmavila.fastnotes.data.note.NoteResponse

interface OnItemClick {
    fun onClickNote(note: NoteResponse, position: Int)
    fun onLongClickNote(note: NoteResponse, position: Int)

}