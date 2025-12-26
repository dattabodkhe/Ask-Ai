package com.example.learningai.model



data class InterviewQuestion(
    val subjectId: String = "",
    val question: String = "",
    val option: List<String> = emptyList(),
    val correctIndex: Int = 0
)