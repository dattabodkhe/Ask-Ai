package com.example.learningai.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
    viewModel: InterviewViewModel
) {
    LaunchedEffect(subjectId) {
        viewModel.loadQuestions(subjectId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val question = viewModel.currentQuestion

    if (question == null) {
        Text("No questions available")
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        Text("Interview Questions", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {

            item {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            itemsIndexed(question.option) { index, option ->
                val isSelected = uiState.selectedOption == index

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { viewModel.selectOption(index) },
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(option, modifier = Modifier.padding(16.dp))
                }
            }
        }

        Button(
            onClick = {
                val isLast = viewModel.submitAndNext()
                if (isLast) onFinish()
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