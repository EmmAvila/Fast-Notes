package com.emmavila.fastnotes.data.note

import com.google.firebase.Timestamp
import java.io.Serializable

data class NoteResponse(
    var id: String? = null,
    val title: String = "",
    val description: String = "",
    @Transient
    val timestamp: Timestamp = Timestamp.now(),
) : Serializable