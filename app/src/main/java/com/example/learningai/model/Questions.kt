package com.example.learningai.model


data class Questions(
    val question: String,
    val option: List<String>,
    val correctAnswerIndex: Int
)
