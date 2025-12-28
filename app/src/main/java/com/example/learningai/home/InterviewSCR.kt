package com.example.learningai.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.learningai.ViewModel.InterviewViewModel

@Composable
fun InterviewScreen(
    subjectId: String,
    viewModel: InterviewViewModel,
    onFinish: () -> Unit
) {
    LaunchedEffect(subjectId) {
        viewModel.loadQuestions(subjectId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val question = viewModel.currentQuestion

    if (uiState.isLoading) {
        CircularProgressIndicator()
        return
    }

    if (question == null) {
        Text("No questions found")
        return
    }

    Column(Modifier.padding(16.dp)) {

        Text(question.question, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        question.option.forEachIndexed { index, text ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable { viewModel.selectOption(index) }
            ) {
                Text(text, Modifier.padding(16.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = uiState.selectedOption != -1,
            onClick = {
                if (viewModel.submitAndNext()) onFinish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uiState.currentIndex == uiState.questions.lastIndex)
                    "Submit"
                else "Next"
            )
        }
    }
}