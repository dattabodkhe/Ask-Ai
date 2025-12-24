package com.example.learningai.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.ViewModel.InterviewViewModel


@Composable
fun InterviewScreen(
    viewModel: InterviewViewModel = viewModel()
) {

    val selectedOption by viewModel.selectedOption.collectAsState()
    val question = viewModel.currentIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Interview Questions",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        QuestionCard(
            question = question,
            selectedOption = selectedOption,
            onOptionSelected = { viewModel.selectedOption(it) }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.nextQuestion() },
            enabled = selectedOption != -1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}