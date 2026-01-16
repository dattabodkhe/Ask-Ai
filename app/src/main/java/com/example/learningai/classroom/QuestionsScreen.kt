package com.example.learningai.classroom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.MVVM.QuestionsViewModel
import com.example.learningai.home.OptionItem
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.nav.Routes
import com.example.learningai.repository.QuestionRepository

@Composable
fun QuestionsScreen(
    navController: NavController,
    classroomId: String
) {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).questionDao()

    val viewModel: QuestionsViewModel = remember {
        QuestionsViewModel(
            QuestionRepository(dao)
        )
    }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(classroomId) {
        viewModel.loadQuestions(classroomId)
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val question = state.questions[state.index]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Question ${state.index + 1} / ${state.questions.size}",
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
                isSelected = state.selected == index,
                isCorrect =
                    if (state.showResult)
                        index == question.correctIndex
                    else null,
                onClick = { viewModel.selectOption(index) }
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if (state.index < state.questions.lastIndex) {
                    viewModel.nextQuestion()
                } else {
                    navController.navigate(
                        "${Routes.RESULT}/$classroomId/${state.score}/${state.questions.size}"
                    )
                }
            },
            enabled = state.showResult,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (state.index == state.questions.lastIndex)
                    "Finish Test"
                else
                    "Next Question"
            )
        }
    }
}