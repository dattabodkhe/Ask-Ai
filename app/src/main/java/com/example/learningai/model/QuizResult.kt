package com.example.learningai.model

import com.google.firebase.Timestamp

data class QuizResult(
    // Query 'senderId' mang rahi hai, isliye hum iska name ya SerializedName change karenge
    val senderId: String = "",
    val classroomId: String = "",
    val score: Int = 0,
    val total: Int = 0, // Profile query me 'total' use ho sakta hai
    // Sabse important: Ye field query filter ke liye zaroori hai
    val type: String = "ai_questions",
    // Firestore ke liye Long se behtar Timestamp hota hai
    val timestamp: Timestamp = Timestamp.now()
)