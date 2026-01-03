package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.model.InterviewQuestion
import com.example.learningai.repository.InterviewRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    private val repo: InterviewRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState

    val currentQuestion: InterviewQuestion?
        get() = _uiState.value.questions.getOrNull(_uiState.value.currentIndex)

    fun loadQuestions(subjectId: String) {

        // ROOM → UI
        viewModelScope.launch {
            repo.getQuestions(subjectId).collect {
                _uiState.value = _uiState.value.copy(
                    questions = it,
                    isLoading = false
                )
            }
        }

        // FIREBASE → ROOM
        viewModelScope.launch {
            repo.syncQuestions(subjectId)
        }
    }

    fun saveResult(subjectId: String) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repo.saveResult(
                subjectId,
                _uiState.value.attemptedCount,
                _uiState.value.correctCount,
                uid
            )
        }
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
    fun selectOption(index: Int) {
        _uiState.value = _uiState.value.copy(
            selectedOption = index
        )
    }
    fun resetQuiz(){
        _uiState.value = InterviewUiState(
            isLoading = true
        )
    }
}