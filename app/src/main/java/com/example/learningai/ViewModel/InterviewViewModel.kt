package com.example.learningai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.learningai.model.Questions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class InterviewViewModel : ViewModel() {

    private val questions = listOf(
        Questions(
            "What is Jetpack Compose?",
            listOf("UI Toolkit", "API", "Database", "Compiler"),
            0
        ),
        Questions(
            "Which language is used for Jetpack Compose?",
            listOf("Java", "Kotlin", "Python", "C++"),
            1
        ),
        Questions(
            "What does MVVM stand for?",
            listOf(
                "Model View ViewModel",
                "Main View Virtual Model",
                "Model Variable View Model",
                "None of these"
            ),
            0
        ),
        Questions(
            "Which component is used for navigation in Jetpack Compose?",
            listOf(
                "Intent",
                "FragmentManager",
                "NavController",
                "Activity"
            ),
            2
        )
    )

    private val _uiState = MutableStateFlow(InterviewUiStates())
    val uiState: StateFlow<InterviewUiStates> = _uiState

    val currentQuestion: Questions
        get() = questions[_uiState.value.currentIndex]

    fun selectOption(index: Int) {
        _uiState.value = _uiState.value.copy(selectedOption = index)
    }

    fun submitAndNext(): Boolean {
        val state = _uiState.value

        val isCorrect =
            state.selectedOption ==
                    questions[state.currentIndex].correctAnswerIndex

        val isLast = state.currentIndex == questions.lastIndex

        _uiState.value = state.copy(
            attemptedCount = state.attemptedCount + 1,
            correctCount = if (isCorrect)
                state.correctCount + 1
            else
                state.correctCount,
            currentIndex = if (isLast)
                state.currentIndex
            else
                state.currentIndex + 1,
            selectedOption = -1
        )

        return isLast
    }

    fun resetQuiz() {
        _uiState.value = InterviewUiStates()
    }

}