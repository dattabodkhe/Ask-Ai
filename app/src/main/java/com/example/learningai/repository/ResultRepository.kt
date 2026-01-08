package com.example.learningai.repo

import com.example.learningai.model.QuizResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ResultRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun saveResult(
        userId: String,
        classroomId: String,
        score: Int,
        totalQuestions: Int
    ) {

        val result = QuizResult(
            userId = userId,
            classroomId = classroomId,
            score = score,
            totalQuestions = totalQuestions,
            timestamp = System.currentTimeMillis()
        )

        db.collection("classroom_results")
            .add(result)
            .await()
    }
}