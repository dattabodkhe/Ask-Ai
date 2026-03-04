package com.example.learningai.localDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Long, // Ye naya column hai jo error de raha tha
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)