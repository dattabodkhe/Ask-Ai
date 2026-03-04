package com.example.learningai.model

import com.example.learningai.localDB.QuestionEntity

data class QuestionsUiState(

    val questions: List<QuestionEntity> = emptyList(),

    val index: Int = 0,

    val selected: Int = -1,

    val score: Int = 0,

    val showResult: Boolean = false,

    val isLoading: Boolean = false,

    val generatedCount: Int = 0,

    val totalCount: Int = 0,

    val difficulty: String = "MEDIUM"  )
