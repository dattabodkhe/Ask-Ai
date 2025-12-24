package com.example.learningai.viewmodel

data class InterviewUiStates(
    val currentIndex: Int = 0,
    val selectedOption: Int = -1,
    val attemptedCount: Int = 0,
    val correctCount: Int = 0,

)