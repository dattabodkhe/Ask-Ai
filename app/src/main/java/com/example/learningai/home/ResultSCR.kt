package com.example.learningai.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.example.learningai.repo.ResultRepository

@Composable
fun ResultScreen(
    classroomId: String,
    score: Int,
    totalQuestions: Int,
    userId: String,
    navController: NavController
) {
    val accuracy = if (totalQuestions > 0)
        (score * 100) / totalQuestions
    else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🎉 Test Completed",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        Text("Score: $score / $totalQuestions")
        Text("Accuracy: $accuracy%")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                ResultRepository.saveResult(
                    userId = userId,
                    classroomId = classroomId,
                    score = score,
                    totalQuestions = totalQuestions
                )

                // 🔙 Back to Home
                navController.popBackStack(Routes.HOME, false)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish")
        }
    }
}