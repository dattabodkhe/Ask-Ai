package com.example.learningai.model

data class ChatItem(
    val name: String,
    val lastMessage: String,
    val time: String
)

fun chatDummyList() = listOf(
    ChatItem("Rahul", "Test shared: Android Basics", "10:45 AM"),
    ChatItem("Aman", "New classroom created", "Yesterday"),
    ChatItem("AI Study Group", "Start test?", "Mon"),
    ChatItem("Neha", "5 questions added", "Sun")
)