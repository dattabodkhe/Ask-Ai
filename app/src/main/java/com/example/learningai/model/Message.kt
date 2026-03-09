package com.example.learningai.model

import com.google.firebase.Timestamp

data class Message(
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Timestamp? = null,
    val type: String = "text",
    val questionList: List<String> = emptyList(),
    val questionCount: Int = 0
)