package com.example.learningai.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningai.model.Questions
@Composable

fun HomeSCR() {

    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf(-1) }

    val sampleQuestions = listOf(
        Questions(
            question = "What is MVVM?",
            option = listOf(
                "Design Pattern",
                "Programming Language",
                "Database",
                "Operating System"
            ),
            correctAnswerIndex = 0
        ),
        Questions(
            question = "What is Jetpack Compose?",
            option = listOf(
                "UI Toolkit",
                "API",
                "Database",
                "Compiler"
            ),
            correctAnswerIndex = 0
        )
    )

    val question = sampleQuestions[currentIndex]

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

        // ✅ CORRECT CALL
        QuestionCard(
            questions = question,
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (currentIndex < sampleQuestions.size - 1) {
                    currentIndex++
                    selectedOption = -1
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedOption != -1
        ) {
            Text(text = "Next")
        }
    }
}
