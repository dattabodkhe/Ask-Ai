// file: InterUiStates.kt
package com.example.learningai.viewmodel

import com.example.learningai.model.InterviewQuestion

data class InterviewUiState(
    val questions: List<InterviewQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOption: Int = -1,
    val correctCount: Int = 0,
    val attemptedCount: Int = 0,
    val isLoading: Boolean = true
)
