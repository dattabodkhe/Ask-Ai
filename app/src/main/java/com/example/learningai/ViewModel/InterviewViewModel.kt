package com.example.learningai.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.Repository.InterviewRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InterviewViewModel : ViewModel() {

    private val repo = InterviewRepository()
    private val auth = FirebaseAuth.getInstance() // ✅ REQUIRED

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState

    val currentQuestion
        get() = _uiState.value.questions
            .getOrNull(_uiState.value.currentIndex)

    // 🔥 LOAD QUESTIONS
    fun loadQuestions(subjectId: String) {
        viewModelScope.launch {
            repo.getQuestionsBySubject(subjectId).collect { list ->
                _uiState.value = _uiState.value.copy(
                    questions = list,
                    isLoading = false,
                    currentIndex = 0,
                    selectedOption = -1,
                    attemptedCount = 0,
                    correctCount = 0
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

        val isCorrect = state.selectedOption == q.correctIndex
        val isLast = state.currentIndex == state.questions.lastIndex

        _uiState.value = state.copy(
            attemptedCount = state.attemptedCount + 1,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
            currentIndex = if (isLast) state.currentIndex else state.currentIndex + 1,
            selectedOption = -1
        )

        return isLast
    }

    // 🔥 RESULT SAVE (ERROR FIXED)
    fun saveResult(subjectId: String) {
        val state = _uiState.value
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            repo.saveResult(
                subjectId = subjectId,
                attempted = state.attemptedCount,
                correct = state.correctCount,
                userId = userId
            )
        }
    }

    fun resetQuiz() {
        _uiState.value = InterviewUiState()
    }
}