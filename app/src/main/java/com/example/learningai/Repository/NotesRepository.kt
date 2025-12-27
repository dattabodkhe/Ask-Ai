package com.example.learningai.repo

import com.example.learningai.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NotesRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getNotes(subjectId: String): Flow<Note> = callbackFlow {

        val listener = firestore
            .collection("notes")
            .document(subjectId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val note = snapshot?.toObject(Note::class.java)
                if (note != null) trySend(note)
            }

        awaitClose { listener.remove() }
    }
}