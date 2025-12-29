package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.Repository.NotesRepository
import com.example.learningai.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val repo = NotesRepository()

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note

    fun loadNotes(subjectId: String) {
        viewModelScope.launch {
            repo.getNoteBySubject(subjectId).collect {
                _note.value = it
            }
        }
    }
}