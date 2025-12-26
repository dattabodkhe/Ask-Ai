package com.example.learningai.home

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.viewmodel.NotesViewModel

@Composable
fun NotesSCR(
    subjectId: String,
    viewModel: NotesViewModel = viewModel()
) {
    LaunchedEffect(subjectId) {
        viewModel.loadNotes(subjectId)
    }

    val note by viewModel.note.collectAsState()

    if (note == null) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Notes",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = note!!.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}