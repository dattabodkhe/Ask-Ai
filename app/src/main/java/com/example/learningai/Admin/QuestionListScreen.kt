package com.example.learningai.Admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable

fun QuestionListScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {
    val questions by viewModel.questions.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestions()
    }

    LazyColumn {
        items(questions) { (docId, q) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(Modifier.padding(12.dp)) {

                    Text(q.question, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(8.dp))

                    Row {
                        Button(onClick = {
                            navController.navigate("edit_question/$docId")
                        }) {
                            Text("Edit")
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(
                            onClick = { viewModel.deleteQuestion(docId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}