package com.example.learningai.classroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.MVVM.QuestionsViewModel
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.QuestionEntity
import com.example.learningai.repository.QuestionRepository


@Composable
fun QuestionsScreen(
    classroomId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).questionDao()
    val viewModel: QuestionsViewModel = remember {
        QuestionsViewModel(
            QuestionRepository(dao)
        )
    }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestions(classroomId)
    }

    if (state.isLoading) {
        Text("Loading questions...")
        return
    }

    val q = state.questions[state.index]

    Column(Modifier.padding(16.dp)) {

        Text("Q${state.index + 1}: ${q.question}")

        listOf(
            q.optionA,
            q.optionB,
            q.optionC,
            q.optionD
        ).forEachIndexed { i, text ->

            Button(
                onClick = { viewModel.selectOption(i) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    if (state.selected == i)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text)
            }
        }

        Button(
            onClick = { viewModel.nextQuestion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }

        Text("Score: ${state.score}")
    }
}