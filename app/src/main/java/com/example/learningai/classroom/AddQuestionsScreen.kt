package com.example.learningai.classroom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.mvvm.AddQuestionsViewModel

@Composable
fun AddQuestionsScreen(
    navController: NavController,
    classroomId: String,
    subject: String,
    totalQuestions: Int,
    viewModel: AddQuestionsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    var questionText by remember { mutableStateOf("") }
    val options = remember { mutableStateListOf("", "", "", "") }
    var correctIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        viewModel.start(
            classroomId = classroomId,
            subject = subject,
            total = totalQuestions
        )
    }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Question ${uiState.currentIndex + 1} / $totalQuestions",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        options.forEachIndexed { index, text ->
            OutlinedTextField(
                value = text,
                onValueChange = { options[index] = it },
                label = { Text("Option ${'A' + index}") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(12.dp))
        Text("Correct Answer")

        options.forEachIndexed { index, _ ->
            Row {
                RadioButton(
                    selected = correctIndex == index,
                    onClick = { correctIndex = index }
                )
                Text("Option ${'A' + index}")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.addQuestion(
                    questionText = questionText,
                    options = options.toList(),
                    correctIndex = correctIndex
                )

                questionText = ""
                options.replaceAll { "" }
                correctIndex = -1
            }
        ) {
            Text("Save & Next")
        }
    }
}