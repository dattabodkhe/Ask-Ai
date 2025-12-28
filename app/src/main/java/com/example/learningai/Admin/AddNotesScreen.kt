package com.example.learningai.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddNotesScreen(
    viewModel: AdminViewModel = viewModel()
) {
    var subject by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        Text("Add Notes", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(subject, { subject = it }, label = { Text("Subject") })
        OutlinedTextField(content, { content = it },
            label = { Text("Notes Content") }, minLines = 5)

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addNotes(subject, content)
        }) {
            Text("Save Notes")
        }
    }
}