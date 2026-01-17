package com.example.learningai.classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.MVVM.QuestionsViewModel
import com.example.learningai.ai.AiRepository
import com.example.learningai.repository.QuestionRepository

class QuestionsViewModelFactory(
    private val repository: QuestionRepository,
    private val aiRepository: AiRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionsViewModel(repository, aiRepository) as T
    }
}