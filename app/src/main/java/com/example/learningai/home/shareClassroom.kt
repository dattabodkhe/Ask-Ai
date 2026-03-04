package com.example.learningai.home

import android.content.Context
import android.content.Intent

fun shareClassroom(
    context: android.content.Context,
    name: String,
    inviteCode: String
) {
    val shareLink = "https://learningai.app/join/$inviteCode"
    val shareMessage = """
        Join my Classroom '$name' on LearningAI 📚
        
        Use this Invite Code: $inviteCode
        
        Or click this link to join directly:
        $shareLink
    """.trimIndent()

    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, shareMessage)
    }

    context.startActivity(android.content.Intent.createChooser(intent, "Share Classroom"))
}