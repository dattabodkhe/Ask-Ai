package com.example.learningai.model

data class QuizResult(
    val userId: String = "",
    val subjectId: String = "",
    val correctCount: Int = 0,
    val attemptedCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)