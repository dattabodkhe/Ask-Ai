package com.example.learningai.repository

import com.example.learningai.localDB.QuestionDao

class QuestionRepository(
    private val dao: QuestionDao
) {
    fun getQuestions(classroomId: String) =
        dao.getQuestions(classroomId)
}