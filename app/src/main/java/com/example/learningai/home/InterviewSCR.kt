package com.example.learningai.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.viewmodel.InterviewViewModel

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

    if (question == null) {
        Text("Loading questions...")
        return
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {

        Text(question.question, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        question.option.forEachIndexed { index, text ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.selectOption(index) }
            ) {
                Text(text, Modifier.padding(16.dp))
            }
        }

        Button(
            onClick = {
                if (viewModel.submitAndNext()) onFinish()
            },
            enabled = uiState.selectedOption != -1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uiState.currentIndex == uiState.questions.lastIndex)
                    "Submit"
                else
                    "Next"
            )
        }
    }
}