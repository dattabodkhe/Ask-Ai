package com.example.learningai.repo

import com.example.learningai.model.QuizResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ResultRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun saveResult(
        userId: String,
        subjectId: String,
        correctCount: Int,
        attemptedCount: Int
    ) {
        val result = QuizResult(
            userId = userId,
            subjectId = subjectId,
            correctCount = correctCount,
            attemptedCount = attemptedCount
        )

        db.collection("quiz_results")
            .add(result)
            .await()
    }
}