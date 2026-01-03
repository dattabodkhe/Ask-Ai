package com.example.learningai.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learningai.model.InterviewQuestion
@Composable
fun EditQuestionScreen(
    docId: String,
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {

    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(List(4) { "" }) }
    var correctIndex by remember { mutableIntStateOf(0) }

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") }
        )

        options.forEachIndexed { index, opt ->
            OutlinedTextField(
                value = opt,
                onValueChange = { newValue ->
                    options = options.toMutableList().also {
                        it[index] = newValue
                    }
                },
                label = { Text("Option ${index + 1}") }
            )
        }

        OutlinedTextField(
            value = correctIndex.toString(),
            onValueChange = { correctIndex = it.toIntOrNull() ?: 0 },
            label = { Text("Correct Index (0-3)") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updateQuestion(
                    docId = docId,
                    updatedQuestion = InterviewQuestion(
                        subjectId = "web_dev",
                        question = question,
                        options = options,
                        correctIndex = correctIndex
                    )
                )
                navController.popBackStack()
            }
        ) {
            Text("Update Question")
        }
    }
}