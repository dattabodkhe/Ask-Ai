package com.example.learningai.viewmodel

import com.example.learningai.model.InterviewQuestion

data class InterviewUiState(
    val attemptedCount: Int = 0,
    val correctCount: Int = 0
)