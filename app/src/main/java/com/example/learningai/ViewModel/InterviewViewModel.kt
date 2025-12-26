package com.example.learningai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.Repo.InterviewRepository
import com.example.learningai.Repo.ResultRepository
import com.example.learningai.model.InterviewQuestion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class InterviewViewModel : ViewModel() {

    private val repository = InterviewRepository()

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState

    val currentQuestion: InterviewQuestion?
        get() = _uiState.value.questions.getOrNull(_uiState.value.currentIndex)

    fun loadQuestions(subjectId: String) {
        viewModelScope.launch {
            repository.getQuestionsBySubject(subjectId)
                .collect { list ->
                    _uiState.value = InterviewUiState(
                        questions = list,
                        isLoading = false
                    )
                }
        }
    }

    fun selectOption(index: Int) {
        _uiState.value = _uiState.value.copy(selectedOption = index)
    }

    fun submitAndNext(): Boolean {
        val state = _uiState.value
        val question = currentQuestion ?: return true

        val isCorrect = state.selectedOption == question.correctIndex
        val isLast = state.currentIndex == state.questions.lastIndex

        _uiState.value = state.copy(
            attemptedCount = state.attemptedCount + 1,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
            currentIndex = if (isLast) state.currentIndex else state.currentIndex + 1,
            selectedOption = -1
        )

        return isLast
    }

    fun saveResult(userId: String, subjectId: String) {
        val state = uiState.value

        ResultRepository.saveResult(
            userId = userId,
            subjectId = subjectId,
            score = state.correctCount,
            attempted = state.attemptedCount
        )
    }
}