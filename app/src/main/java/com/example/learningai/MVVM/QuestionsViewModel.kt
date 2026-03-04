package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.model.QuestionsUiState
import com.example.learningai.repository.QuestionRepository
import kotlinx.coroutines.delay
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
        total: Int,
        dao: QuestionDao
    ) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    totalCount = total,
                    generatedCount = 0
                )
            }

            val existing = repository.getOnce(classroomId)
            if (existing.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        questions = existing,
                        isLoading = false,
                        generatedCount = existing.size
                    )
                }
                return@launch
            }

            generateQuestionsInBatches(
                total = total,
                subject = subject,
                classroomId = classroomId,
                dao = dao
            )

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

    private suspend fun generateQuestionsInBatches(
        total: Int,
        batchSize: Int = 5,
        subject: String,
        classroomId: String,
        dao: QuestionDao
    ) {
        var remaining = total
        var generated = 0

        while (remaining > 0) {
            val currentBatch = minOf(batchSize, remaining)

            // 🔥 FINAL FIX HERE (dao = dao)
            aiRepository.generateAndSaveQuestions(
                classroomId = classroomId,
                subject = subject,
                count = currentBatch,
                dao = dao
            )

            generated += currentBatch
            remaining -= currentBatch

            _uiState.update {
                it.copy(generatedCount = generated)
            }

            delay(1200)
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