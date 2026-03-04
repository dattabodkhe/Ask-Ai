package com.example.learningai.model

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage("Hi 👋 Ask me anything!", false)
    ),
    val isTyping: Boolean = false,
    val isLimitExceeded: Boolean = false,
    val errorMessage: String? = null
)