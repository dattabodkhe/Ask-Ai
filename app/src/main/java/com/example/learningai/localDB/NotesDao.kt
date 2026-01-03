package com.example.learningai.localDB

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes WHERE subjectId = :subjectId LIMIT 1")
    fun getNote(subjectId: String): Flow<NotesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NotesEntity)
}