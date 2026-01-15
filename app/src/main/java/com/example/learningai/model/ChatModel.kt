package com.example.learningai.model

import android.icu.util.TimeZone

data class ChatModel(
    val classroomId: String,
    val title: String,
    val name : String,
    val lastMessage : String ,
    val time : String
)