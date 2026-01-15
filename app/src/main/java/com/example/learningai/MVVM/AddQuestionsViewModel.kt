package com.example.learningai.MVVM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.localDB.QuestionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AddQuestionsUiState(
    val currentIndex: Int = 0,
    val totalQuestions: Int = 0,
    val isFinished: Boolean = false
)

class AddQuestionsViewModel(
    application1: AiRepository,
    application: Application
) : AndroidViewModel(application) {

    private val questionDao =
        AppDatabase.getDatabase(application).questionDao()

    private val _uiState = MutableStateFlow(AddQuestionsUiState())
    val uiState: StateFlow<AddQuestionsUiState> = _uiState

    private lateinit var classroomId: String
    private lateinit var subject: String

    fun start(
        classroomId: String,
        subject: String,
        total: Int
    ) {
        this.classroomId = classroomId
        this.subject = subject

        _uiState.value = AddQuestionsUiState(
            currentIndex = 0,
            totalQuestions = total,
            isFinished = false
        )
    }

    fun addQuestion(
        questionText: String,
        options: List<String>,
        correctIndex: Int
    ) {
        if (
            questionText.isBlank() ||
            options.any { it.isBlank() } ||
            correctIndex == -1
        ) return

        viewModelScope.launch {
            val entity = QuestionEntity(
                classroomId = classroomId,
                question = questionText,
                optionA = options[0],
                optionB = options[1],
                optionC = options[2],
                optionD = options[3],
                correctIndex = correctIndex
            )

            questionDao.insert(entity)

            val next = _uiState.value.currentIndex + 1
            _uiState.value = _uiState.value.copy(
                currentIndex = next,
                isFinished = next >= _uiState.value.totalQuestions
            )
        }
    }
}