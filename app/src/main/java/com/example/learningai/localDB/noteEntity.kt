package com.example.learningai.localDB



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey val subjectId: String,
    val title: String,
    val content: String
)