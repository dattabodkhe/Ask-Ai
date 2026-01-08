package com.example.learningai.ai

data class AiQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)