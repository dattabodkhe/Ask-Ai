package com.example.learningai.localDB

import com.example.learningai.model.Note
import com.example.learningai.model.InterviewQuestion

/* ---------- NOTES ---------- */

fun Note.toEntity(): NotesEntity {
    return NotesEntity(
        subjectId = subjectId,
        title = title,
        content = content
    )
}

fun NotesEntity.toModel(): Note {
    return Note(
        subjectId = subjectId,
        title = title,
        content = content
    )
}

/* ---------- QUESTIONS ---------- */

fun InterviewQuestion.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        subjectId = subjectId,
        question = question,
        options = options,
        correctIndex = correctIndex
    )
}

fun QuestionEntity.toModel(): InterviewQuestion {
    return InterviewQuestion(
        id = id,
        subjectId = subjectId,
        question = question,
        options = options,
        correctIndex = correctIndex
    )
}