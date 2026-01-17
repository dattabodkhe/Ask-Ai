package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.model.QuestionsUiState
import com.example.learningai.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionsViewModel(
    private val repository: QuestionRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionsUiState())
    val uiState: StateFlow<QuestionsUiState> = _uiState

    fun loadOrGenerateQuestions(
        classroomId: String,
        subject: String,
        count: Int,
        dao: QuestionDao
    ) {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            val existing = repository.getOnce(classroomId)

            // ✅ TEMP: AI ke bajay dummy questions insert
            if (existing.isEmpty()) {
                aiRepository.insertDummyQuestions(
                    classroomId = classroomId,
                    questionDao = dao
                )
            }

            // ✅ DB observe (THIS IS IMPORTANT)
            repository.getQuestions(classroomId)
                .collectLatest { list ->
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
        if (state.showResult) return

        val correctIndex = state.questions[state.index].correctIndex

        _uiState.update {
            it.copy(
                selected = optionIndex,
                score = if (optionIndex == correctIndex) it.score + 1 else it.score,
                showResult = true
            )
        }
    }

    fun nextQuestion() {
        _uiState.update {
            it.copy(
                index = it.index + 1,
                selected = -1,
                showResult = false
            )
        }
    }
}