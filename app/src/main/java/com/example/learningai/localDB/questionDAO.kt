package com.example.learningai.localDB


import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE subjectId = :subjectId")
    fun getQuestionsBySubject(subjectId: String): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<QuestionEntity>)

    @Query("DELETE FROM questions WHERE subjectId = :subjectId")
    suspend fun clearBySubject(subjectId: String)
}