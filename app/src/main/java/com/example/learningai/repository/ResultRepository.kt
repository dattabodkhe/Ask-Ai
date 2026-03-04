package com.example.learningai.repo

import android.util.Log
import com.example.learningai.model.QuizResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

object ResultRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun saveResult(
        userId: String,
        classroomId: String,
        score: Int,
        totalQuestions: Int
    ) {
        try {
            val result = QuizResult(
                senderId = userId,
                classroomId = classroomId,
                score = score,
                total = totalQuestions,
                type = "ai_questions",
                timestamp = Timestamp.now()
            )

            db.collection("classrooms")
                .document(classroomId)
                .collection("messages")
                .add(result)
                .await()

            Log.d("REPO", "Result successfully saved! Profile count update ho jayega.")
        } catch (e: Exception) {
            Log.e("REPO", "Error saving result: ${e.message}")
        }
    }
}