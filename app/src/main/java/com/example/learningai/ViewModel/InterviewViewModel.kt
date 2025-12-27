package com.example.learningai.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InterviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState

    fun incrementAttempt(correct: Boolean) {
        _uiState.value = _uiState.value.copy(
            attemptedCount = _uiState.value.attemptedCount + 1,
            correctCount = if (correct)
                _uiState.value.correctCount + 1
            else
                _uiState.value.correctCount
        )
    }

    fun resetQuiz() {
        _uiState.value = InterviewUiState()
    }
}