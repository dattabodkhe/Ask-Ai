package com.example.learningai.Repo

import com.google.firebase.firestore.FirebaseFirestore

object ResultRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveResult(
        userId: String,
        subjectId: String,
        score: Int,
        attempted: Int
    ) {
        val data = hashMapOf(
            "userId" to userId,
            "subjectId" to subjectId,
            "score" to score,
            "attempted" to attempted,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("results")
            .add(data)
    }
}