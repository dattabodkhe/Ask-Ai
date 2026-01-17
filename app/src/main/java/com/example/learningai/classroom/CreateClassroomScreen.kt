package com.example.learningai.classroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.model.Difficulty
import com.example.learningai.nav.Routes
import com.example.learningai.ui.theme.appGradient
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassroomScreen(
    navController: NavController
) {
    var classroomName by remember { mutableStateOf("") }
    var questionCount by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // ✅ Difficulty state
    var difficulty by remember { mutableStateOf(Difficulty.EASY) }

    val subjects = listOf(
        "Android Development",
        "Web Development",
        "DSA",
        "AI / ML",
        "Computer Basics"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Create AI Classroom",
                    style = MaterialTheme.typography.headlineSmall
                )

                /* Classroom Name */
                OutlinedTextField(
                    value = classroomName,
                    onValueChange = { classroomName = it },
                    label = { Text("Classroom Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                /* Subject */
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

                /* Number of Questions */
                OutlinedTextField(
                    value = questionCount,
                    onValueChange = { questionCount = it },
                    label = { Text("Number of Questions") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                /* ✅ Difficulty Selector */
                Text("Difficulty Level")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Difficulty.values().forEach { level ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = difficulty == level,
                                onClick = { difficulty = level }
                            )
                            Text(level.name)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                /* Generate with AI */
                Button(
                    onClick = {
                        if (
                            selectedSubject.isNotBlank() &&
                            questionCount.toIntOrNull() != null
                        ) {
                            val classroomId = UUID.randomUUID().toString()
                            val count = questionCount.toInt()

                            navController.navigate(
                                "${Routes.QUESTIONSCREEN}/$classroomId/$selectedSubject/$count/${difficulty.name}"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Generate Question")
                }
            }
        }
    }
}