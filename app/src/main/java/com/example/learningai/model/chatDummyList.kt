package com.example.learningai.model

import com.google.type.TimeZone

fun chatDummyList(): List<ChatModel> {
    return listOf(
        ChatModel(
            classroomId = "class12345",
            title = "Android Basics",
            name = "DATTA ",
            lastMessage = "hi",
            time ="10Pm"
        ),
        ChatModel(
            classroomId = "class67890",
            title = "Kotlin Fundamentals",
            name = "depak",
            lastMessage = "hi",
            time = "10Pm"
        )
    )
}