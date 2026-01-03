package com.example.learningai.localDB

import android.content.Context
import androidx.room.Room
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.NotesDao
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "learning_ai_db"
        ).build()

    @Provides
    fun provideQuestionDao(db: AppDatabase): QuestionDao =
        db.questionDao()

    @Provides
    fun provideNotesDao(db: AppDatabase): NotesDao =
        db.notesDao()
}