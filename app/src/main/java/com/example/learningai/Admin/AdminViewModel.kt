package com.example.learningai.Admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.model.InterviewQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    // 🔐 Admin flag
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    // 🔔 UI message
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // 📦 Question list (docId → Question)
    private val _questions =
        MutableStateFlow<List<Pair<String, InterviewQuestion>>>(emptyList())
    val questions: StateFlow<List<Pair<String, InterviewQuestion>>> = _questions

    // TEMP ADMIN CHECK
    fun checkAdmin() {
        _isAdmin.value = true
    }

    // 📥 LOAD QUESTIONS (mock / later Firestore)
    fun loadQuestions() {
        viewModelScope.launch {
            _questions.value = listOf(
                "doc1" to InterviewQuestion(
                    subjectId = "web_dev",
                    question = "What is HTML?",
                    options = listOf("Lang", "Protocol", "DB", "OS"),
                    correctIndex = 0
                )
            )
        }
    }

    // ➕ ADD QUESTION
    fun addQuestion(
        subjectId: String,
        question: String,
        options: List<String>,
        correctIndex: Int
    ) {
        _message.value = "Question Added ✅"
    }

    // 🗑 DELETE QUESTION
    fun deleteQuestion(docId: String) {
        _questions.value = _questions.value.filterNot { it.first == docId }
        _message.value = "Question Deleted 🗑"
    }

    // ✏️ UPDATE QUESTION (✅ FIXED)
    fun updateQuestion(
        docId: String,
        updatedQuestion: InterviewQuestion
    ) {
        _questions.value = _questions.value.map {
            if (it.first == docId) docId to updatedQuestion else it
        }
        _message.value = "Question Updated ✏️"
    }

    // 📝 ADD NOTES
    fun addNotes(subject: String, content: String) {
        _message.value = "Notes Added 📝"
    }
}