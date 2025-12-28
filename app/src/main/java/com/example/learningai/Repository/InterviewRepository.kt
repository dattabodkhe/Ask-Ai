package com.example.learningai.Repository

import com.example.learningai.model.InterviewQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class InterviewRepository {

    private val db = FirebaseFirestore.getInstance()

    private val questionRef = db.collection("questions")
    private val resultRef = db.collection("results") // ✅ NEW

    // 🔹 USER – LIVE QUESTIONS
    fun getQuestionsBySubject(subjectId: String): Flow<List<InterviewQuestion>> =
        callbackFlow {
            val listener = questionRef
                .whereEqualTo("subjectId", subjectId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val list = snapshot?.documents
                        ?.mapNotNull { it.toObject(InterviewQuestion::class.java) }
                        ?: emptyList()

                    trySend(list)
                }

            awaitClose { listener.remove() }
        }

    // 🔹 SAVE RESULT ✅
    suspend fun saveResult(
        subjectId: String,
        attempted: Int,
        correct: Int,
        userId: String
    ) {
        val data = mapOf(
            "subjectId" to subjectId,
            "attempted" to attempted,
            "correct" to correct,
            "userId" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        resultRef.add(data).await()
    }

    // 🔹 ADMIN
    suspend fun addQuestion(question: InterviewQuestion) {
        questionRef.add(question).await()
    }

    suspend fun updateQuestion(docId: String, updated: InterviewQuestion) {
        questionRef.document(docId).set(updated).await()
    }

    suspend fun deleteQuestion(docId: String) {
        questionRef.document(docId).delete().await()
    }
}