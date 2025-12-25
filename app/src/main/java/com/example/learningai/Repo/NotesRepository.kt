package com.example.learningai.Repo


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotesRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getNotes(subjectId: String): String {
        val doc = db.collection("notes")
            .document(subjectId)
            .get()
            .await()

        return doc.getString("content") ?: "No notes found"
    }
}