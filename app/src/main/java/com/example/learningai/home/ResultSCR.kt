package com.example.learningai.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.viewmodel.InterviewViewModel

@Composable

fun ResultScreen(
    viewModel: InterviewViewModel = viewModel(),
    onRetry: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🎉 Test Completed",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Questions Attempted: ${uiState.attemptedCount}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Correct Answers: ${uiState.correctCount}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Wrong Answers: ${uiState.attemptedCount - uiState.correctCount}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        LinearProgressIndicator(
            progress = if (uiState.attemptedCount == 0) 0f
            else uiState.correctCount.toFloat() / uiState.attemptedCount,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.resetQuiz()
                onRetry()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retry Test")
        }
    }
}