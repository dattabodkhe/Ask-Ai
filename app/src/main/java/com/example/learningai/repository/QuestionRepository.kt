package com.example.learningai.repository

import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.QuestionEntity
import kotlinx.coroutines.flow.Flow


class QuestionRepository(
    private val dao: QuestionDao
) {


    fun getQuestions(classroomId: String): Flow<List<QuestionEntity>> {
        return dao.getQuestions(classroomId)
    }


    suspend fun getOnce(classroomId: String): List<QuestionEntity> {
        return dao.getQuestionsOnce(classroomId)
    }
}