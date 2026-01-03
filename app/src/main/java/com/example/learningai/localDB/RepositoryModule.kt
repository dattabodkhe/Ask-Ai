package com.example.learningai.localDB

import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.NotesDao
import com.example.learningai.repository.InterviewRepository
import com.example.learningai.repository.NotesRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideInterviewRepository(
        dao: QuestionDao
    ): InterviewRepository =
        InterviewRepository(dao)

    @Provides
    @Singleton
    fun provideNotesRepository(
        dao: NotesDao
    ): NotesRepository =
        NotesRepository(dao)
}