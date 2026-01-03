package com.example.learningai.repository

import com.example.learningai.localDB.NotesDao
import com.example.learningai.localDB.toEntity
import com.example.learningai.localDB.toModel
import com.example.learningai.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class NotesRepository(
    private val dao: NotesDao
) {

    private val firestore = FirebaseFirestore.getInstance()
    private val notesRef = firestore.collection("notes")

    fun getNote(subjectId: String): Flow<Note?> {
        return dao.getNote(subjectId).map { it?.toModel() }
    }

    suspend fun syncNote(subjectId: String) {
        val snap = notesRef.document(subjectId).get().await()
        snap.toObject(Note::class.java)?.let {
            dao.insert(it.toEntity())
        }

    }
}
