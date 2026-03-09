package com.example.learningai.MVVM

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.ai.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestionsPreviewViewModel(private val context: Context) : ViewModel() {
    private val repository = AiRepository(context)
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Real questions store karne ke liye
    val questionsList = mutableStateListOf<String>()

    fun generateQuestions(subject: String, count: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            questionsList.clear()
            
            // AI se questions mangne ka prompt
            val prompt = "Generate $count important questions for the subject '$subject'. Return only a list of questions separated by new lines."
            val response = repository.chat(prompt)
            
            // Response ko list mein convert karna
            val questions = response.split("\n")
                .filter { it.isNotBlank() }
                .map { it.replace(Regex("^\\d+\\.\\s*"), "").trim() } // Numbering hatane ke liye
            
            questionsList.addAll(questions)
            _isLoading.value = false
        }
    }
}