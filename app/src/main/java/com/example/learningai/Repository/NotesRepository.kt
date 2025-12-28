package com.example.learningai.Repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class Note(
    val title: String = "",
    val content: String = ""
)

class NotesRepository {

    private val db = FirebaseFirestore.getInstance()
    private val notesRef = db.collection("notes")

    fun getNoteBySubject(subjectId: String): Flow<Note?> = callbackFlow {
        val listener = notesRef
            .document(subjectId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                trySend(snapshot?.toObject(Note::class.java))
            }

        awaitClose { listener.remove() }
    }
}