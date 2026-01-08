package com.example.learningai.mvvm

import com.example.learningai.localDB.QuestionEntity

data class ClassroomQuizUiState(
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOption: Int = -1,
    val attemptedCount: Int = 0,
    val correctCount: Int = 0,
    val isLoading: Boolean = true,
    val isFinished: Boolean = false
)