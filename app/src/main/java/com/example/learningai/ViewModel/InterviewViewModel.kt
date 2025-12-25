package com.example.learningai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.learningai.model.Questions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class InterviewViewModel : ViewModel() {

    private val allQuestions = listOf(

        Questions("app_dev","What is Jetpack Compose?",
            listOf("UI Toolkit","API","Database","Compiler"),0),

        Questions("app_dev","Which language is used for Jetpack Compose?",
            listOf("Java","Kotlin","Python","C++"),1),

        Questions("web_dev","What does HTML stand for?",
            listOf("Hyper Text Markup Language","High Text ML","Hyperlinks ML","None"),0),

        Questions("dsa","Which data structure uses FIFO?",
            listOf("Stack","Queue","Tree","Graph"),1)
    )

    private val _uiState = MutableStateFlow(InterviewUiStates())
    val uiState: StateFlow<InterviewUiStates> = _uiState

    // 🔥 SUBJECT-BASED LOAD (PROPER)
    fun loadQuestions(subjectId: String) {
        val filtered = allQuestions.filter { it.subjectId == subjectId }

        _uiState.value = InterviewUiStates(
            questions = filtered
        )
    }

    // 🔒 SAFE CURRENT QUESTION
    val currentQuestion: Questions?
        get() = _uiState.value.questions
            .getOrNull(_uiState.value.currentIndex)

    fun selectOption(index: Int) {
        _uiState.value = _uiState.value.copy(selectedOption = index)
    }

    fun submitAndNext(): Boolean {
        val state = _uiState.value
        val questions = state.questions

        if (questions.isEmpty()) return true

        val isCorrect =
            state.selectedOption ==
                    questions[state.currentIndex].correctAnswerIndex

        val isLast = state.currentIndex == questions.lastIndex

        _uiState.value = state.copy(
            attemptedCount = state.attemptedCount + 1,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
            currentIndex = if (isLast) state.currentIndex else state.currentIndex + 1,
            selectedOption = -1
        )

        return isLast
    }

    fun resetQuiz() {
        _uiState.value = InterviewUiStates()
    }
}