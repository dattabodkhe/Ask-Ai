package com.example.learningai.classroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassroomScreen(
    navController: NavController
) {
    var classroomName by remember { mutableStateOf("") }
    var questionCount by remember { mutableStateOf("") }

    val subjects = listOf(
        "Android Development",
        "Web Development",
        "DSA",
        "AI / ML",
        "Computer Basics"
    )

    var selectedSubject by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = classroomName,
            onValueChange = { classroomName = it },
            label = { Text("Classroom Name") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedSubject,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Subject") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject) },
                        onClick = {
                            selectedSubject = subject
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = questionCount,
            onValueChange = { questionCount = it },
            label = { Text("Number of Questions") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (
                    classroomName.isNotBlank() &&
                    selectedSubject.isNotBlank() &&
                    questionCount.isNotBlank()
                ) {
                    navController.navigate(Routes.QUESTIONSCREEN)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Classroom")
        }
    }
}