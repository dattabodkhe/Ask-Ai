package com.example.learningai.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learningai.ai.AiQuestion
import com.example.learningai.home.Classroom
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE classroomId = :classroomId")
    fun getQuestions(classroomId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE classroomId = :classroomId")
    suspend fun getQuestionsOnce(classroomId: String): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)
}