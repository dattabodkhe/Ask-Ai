package com.example.learningai.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.home.OptionItem
import com.example.learningai.localDB.QuestionEntity
import com.example.learningai.nav.Routes

@Composable
fun QuestionsScreen(
    navController: NavController,
    classroomId: String,
    questions: List<QuestionEntity>
) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf(-1) }
    var correctCount by remember { mutableStateOf(0) }
    var attemptedCount by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val question = questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Question ${currentIndex + 1} / ${questions.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        val options = listOf(
            question.optionA,
            question.optionB,
            question.optionC,
            question.optionD
        )

        options.forEachIndexed { index, option ->
            OptionItem(
                text = option,
                isSelected = selectedOption == index,
                isCorrect = if (showResult) index == question.correctIndex else null,
                onClick = {
                    if (!showResult) {
                        selectedOption = index
                        attemptedCount++
                        showResult = true
                        if (index == question.correctIndex) {
                            correctCount++
                        }
                    }
                }
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if (currentIndex < questions.lastIndex) {
                    currentIndex++
                    selectedOption = -1
                    showResult = false
                } else {
                    navController.navigate(
                        "${Routes.RESULT}/$classroomId/$correctCount/${questions.size}"
                    )
                }
            },
            enabled = showResult,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (currentIndex == questions.lastIndex)
                    "Finish Test"
                else
                    "Next Question"
            )
        }
    }
}