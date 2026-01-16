package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.model.QuestionsUiState
import com.example.learningai.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionsViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionsUiState())
    val uiState: StateFlow<QuestionsUiState> = _uiState

    fun loadQuestions(classroomId: String) {
        viewModelScope.launch {
            repository.getQuestions(classroomId).collect { list ->
                _uiState.update {
                    it.copy(
                        questions = list,
                        isLoading = false
                    )
                }
            }
        }
    }


    fun selectOption(optionIndex: Int) {
        val state = _uiState.value
        if (state.showResult) return   // already answered

        val correctIndex =
            state.questions[state.index].correctIndex

        _uiState.update {
            it.copy(
                selected = optionIndex,
                score = if (optionIndex == correctIndex)
                    it.score + 1
                else
                    it.score,
                showResult = true
            )
        }
    }


    fun nextQuestion() {
        val state = _uiState.value

        if (state.index < state.questions.lastIndex) {
            _uiState.update {
                it.copy(
                    index = it.index + 1,
                    selected = -1,
                    showResult = false   // 🔥 reset for next question
                )
            }
        }
    }
}