package com.example.learningai.classroom

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.MVVM.QuestionsViewModel
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.repository.QuestionRepository

class QuestionsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(QuestionsViewModel::class.java)) {

            val dao = AppDatabase.getDatabase(context).questionDao()

            val repository = QuestionRepository(dao)

            val aiRepository = AiRepository(context)

            return QuestionsViewModel(
                repository,
                aiRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}