package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.model.Note
import com.example.learningai.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repo: NotesRepository
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note

    fun loadNotes(subjectId: String) {

        // ROOM → UI
        viewModelScope.launch {
            repo.getNote(subjectId).collect {
                _note.value = it
            }
        }

        // FIREBASE → ROOM
        viewModelScope.launch {
            repo.syncNote(subjectId)
        }
    }
}