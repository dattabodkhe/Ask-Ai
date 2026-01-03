package com.example.learningai.repository

import android.R.attr.id
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.QuestionEntity
import com.example.learningai.localDB.toEntity
import com.example.learningai.localDB.toModel
import com.example.learningai.model.InterviewQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class InterviewRepository(
    private val dao: QuestionDao
) {

    private val firestore = FirebaseFirestore.getInstance()
    private val questionRef = firestore.collection("questions")
    private val resultRef = firestore.collection("results")

    // 🔹 ROOM → UI
    fun getQuestions(subjectId: String): Flow<List<InterviewQuestion>> {
        return dao.getQuestionsBySubject(subjectId).map { list ->
            list.map { it.toModel() }
        }
    }

    // 🔹 FIREBASE → ROOM (SYNC)
    suspend fun syncQuestions(subjectId: String) {
        val snapshot = questionRef
            .whereEqualTo("subjectId", subjectId)
            .get()
            .await()

        val entities = snapshot.documents.mapNotNull {
            it.toObject(InterviewQuestion::class.java)?.toEntity()
        }

        dao.clearBySubject(subjectId)
        dao.insertAll(entities)
    }

    // 🔹 SAVE RESULT (Firebase)
    suspend fun saveResult(
        subjectId: String,
        attempted: Int,
        correct: Int,
        userId: String
    ) {
        resultRef.add(
            mapOf(
                "subjectId" to subjectId,
                "attempted" to attempted,
                "correct" to correct,
                "userId" to userId,
                "timestamp" to System.currentTimeMillis()
            )
        ).await()
    }
}
