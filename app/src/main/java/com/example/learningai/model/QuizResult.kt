package com.example.learningai.model

data class QuizResult(
    val userId: String = "",
    val classroomId: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)