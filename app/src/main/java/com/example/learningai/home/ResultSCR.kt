package com.example.learningai.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.example.learningai.repo.ResultRepository
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(
    classroomId: String,
    score: Int,
    totalQuestions: Int,
    userId: String,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("🎉 Test Completed", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text("Score: $score / $totalQuestions")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    ResultRepository.saveResult(
                        userId = userId,
                        classroomId = classroomId,
                        score = score,
                        totalQuestions = totalQuestions
                    )
                    navController.popBackStack(Routes.HOME, false)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish")
        }
    }
}