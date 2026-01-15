package com.example.learningai.model

import com.example.learningai.localDB.QuestionEntity

data class QuestionsUiState(
    val questions: List<QuestionEntity> = emptyList(),
    val index: Int = 0,
    val selected: Int = -1,
    val score: Int = 0,
    val isLoading: Boolean = true
)