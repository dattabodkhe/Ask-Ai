package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.Repository.InterviewRepository
import com.example.learningai.model.InterviewQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InterviewViewModel : ViewModel() {

    private val repo = InterviewRepository()

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState

    val currentQuestion: InterviewQuestion?
        get() = _uiState.value.questions.getOrNull(_uiState.value.currentIndex)

    fun loadQuestions(subjectId: String) {
        viewModelScope.launch {
            repo.getQuestionsBySubject(subjectId).collect { list ->
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
        val q = currentQuestion ?: return true

        val correct = state.selectedOption == q.correctIndex
        val last = state.currentIndex == state.questions.lastIndex

        _uiState.value = state.copy(
            attemptedCount = state.attemptedCount + 1,
            correctCount = if (correct) state.correctCount + 1 else state.correctCount,
            currentIndex = if (last) state.currentIndex else state.currentIndex + 1,
            selectedOption = -1
        )

        return last
    }
    // ✅ ADD THIS (CRASH FIX)
    fun saveResult(subjectId: String) {
        viewModelScope.launch {
            repo.saveResult(
                subjectId = subjectId,
                attempted = _uiState.value.attemptedCount,
                correct = _uiState.value.correctCount,
                userId =""
            )
        }
    }



    fun resetQuiz() {
        _uiState.value = InterviewUiState()
    }
}
