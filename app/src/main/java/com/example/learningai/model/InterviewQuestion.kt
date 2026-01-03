package com.example.learningai.model

data class InterviewQuestion(
    val id: String = "",
    val subjectId: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,

)