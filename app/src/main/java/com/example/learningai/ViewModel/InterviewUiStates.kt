package com.example.learningai.viewmodel

import com.example.learningai.model.Questions

data class InterviewUiStates(
    val questions: List<Questions> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOption: Int = -1,
    val attemptedCount: Int = 0,
    val correctCount: Int = 0
)