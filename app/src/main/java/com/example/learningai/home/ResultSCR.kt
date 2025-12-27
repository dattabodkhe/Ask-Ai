package com.example.learningai.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningai.viewmodel.InterviewViewModel
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(
    subjectId: String,
    viewModel: InterviewViewModel,
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("🎉 Test Completed", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        Text("Attempted: ${uiState.attemptedCount}")
        Text("Correct: ${uiState.correctCount}")
        Text("Wrong: ${uiState.attemptedCount - uiState.correctCount}")

        Spacer(Modifier.height(16.dp))

        LinearProgressIndicator(
        progress = {
            if (uiState.attemptedCount == 0) 0f
                    else uiState.correctCount.toFloat() / uiState.attemptedCount
        },
        modifier = Modifier.fillMaxWidth(),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            viewModel.saveResult(subjectId)
            viewModel.resetQuiz()
            onFinish()
        }) {
            Text("Finish")
        }
    }
}