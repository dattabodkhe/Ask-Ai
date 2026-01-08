package com.example.learningai.classroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningai.localDB.QuestionDao

@Composable
fun QuestionsScreen(
    classroomId: String,
    questionDao: QuestionDao
) {
    val questions by questionDao
        .getQuestions(classroomId)
        .collectAsState(initial = emptyList())

    var index by remember { mutableStateOf(0) }
    var selected by remember { mutableStateOf(-1) }
    var score by remember { mutableStateOf(0) }

    if (questions.isEmpty()) {
        Text("Loading questions...")
        return
    }

    val q = questions[index]

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Q${index + 1}: ${q.question}")

        val options = listOf(
            q.optionA, q.optionB, q.optionC, q.optionD
        )

        options.forEachIndexed { i, text ->
            Button(
                onClick = { selected = i },
                colors = ButtonDefaults.buttonColors(
                    if (selected == i) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text)
            }
        }

        Button(
            onClick = {
                if (selected == q.correctIndex) score++

                if (index < questions.lastIndex) {
                    index++
                    selected = -1
                }
            }
        ) {
            Text("Next")
        }

        Text("Score: $score")
    }
}