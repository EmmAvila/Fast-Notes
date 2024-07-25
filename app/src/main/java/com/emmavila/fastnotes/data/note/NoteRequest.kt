package com.emmavila.fastnotes.data.note

import com.google.firebase.Timestamp


data class NoteRequest(
    val title: String = "",
    val description: String = "",
    val timestamp: Timestamp = Timestamp.now(),
)