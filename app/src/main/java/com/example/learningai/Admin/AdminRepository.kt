package com.example.learningai.Admin

import com.example.learningai.model.InterviewQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllQuestions(): List<Pair<String, InterviewQuestion>> {
        val snap = db.collection("interview_questions").get().await()
        return snap.documents.mapNotNull { doc ->
            doc.toObject(InterviewQuestion::class.java)?.let {
                doc.id to it
            }
        }
    }

    suspend fun deleteQuestion(docId: String) {
        db.collection("interview_questions").document(docId).delete().await()
    }

    suspend fun updateQuestion(
        docId: String,
        question: InterviewQuestion
    ) {
        db.collection("interview_questions")
            .document(docId)
            .set(question)
            .await()
    }
}