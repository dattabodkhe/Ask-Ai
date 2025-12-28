package com.example.learningai.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddQuestionScreen(
    viewModel: AdminViewModel = viewModel()
) {
    var subjectId by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var opt1 by remember { mutableStateOf("") }
    var opt2 by remember { mutableStateOf("") }
    var opt3 by remember { mutableStateOf("") }
    var opt4 by remember { mutableStateOf("") }
    var correctIndex by remember { mutableStateOf("0") }

    val message by viewModel.message.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Add Interview Question", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(subjectId, { subjectId = it }, label = { Text("Subject ID") })
        OutlinedTextField(question, { question = it }, label = { Text("Question") })

        OutlinedTextField(opt1, { opt1 = it }, label = { Text("Option 1") })
        OutlinedTextField(opt2, { opt2 = it }, label = { Text("Option 2") })
        OutlinedTextField(opt3, { opt3 = it }, label = { Text("Option 3") })
        OutlinedTextField(opt4, { opt4 = it }, label = { Text("Option 4") })

        OutlinedTextField(correctIndex, { correctIndex = it },
            label = { Text("Correct Index (0-3)") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addQuestion(
                subjectId,
                question,
                listOf(opt1, opt2, opt3, opt4),
                correctIndex.toIntOrNull() ?: 0
            )
        }) {
            Text("Save Question")
        }

        message?.let { Text(it) }
    }
}