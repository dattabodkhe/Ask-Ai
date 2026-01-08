package com.example.learningai.home

import android.content.Context
import android.content.Intent

fun shareClassroom(
    context: Context,
    code: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Join my classroom on LearningAI 📚\nClassroom Code: $code"
        )
    }

    context.startActivity(
        Intent.createChooser(intent, "Share Classroom")
    )
}