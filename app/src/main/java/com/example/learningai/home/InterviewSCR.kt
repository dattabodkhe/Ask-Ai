package com.example.learningai.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningai.MVVM.InterviewViewModel


import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun InterviewScreen(
    subjectId: String,
    onFinish: () -> Unit,
    viewModel: InterviewViewModel = viewModel()
) {

    LaunchedEffect(subjectId) {
        viewModel.loadQuestions(subjectId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val question = viewModel.currentQuestion

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (question == null) {
        Text("No questions found")
        return
    }

    // 👇 SAB UI IS COLUMN KE ANDAR
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        question.options.forEachIndexed { index, text ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.selectOption(index) },
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (uiState.selectedOption == index)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text, Modifier.padding(16.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = uiState.selectedOption != -1,
            onClick = {
                if (viewModel.submitAndNext()) {
                    viewModel.saveResult(subjectId)
                    onFinish()
                }
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