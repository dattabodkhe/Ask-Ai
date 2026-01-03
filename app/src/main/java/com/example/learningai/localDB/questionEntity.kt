package com.example.learningai.localDB


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val question: String,

    val options: List<String>,

    val correctIndex: Int
)