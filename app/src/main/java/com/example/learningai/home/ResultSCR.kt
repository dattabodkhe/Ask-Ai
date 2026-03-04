package com.example.learningai.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val percentage = if (totalQuestions == 0) 0 else (score * 100 / totalQuestions)
    val purpleGradient = Brush.verticalGradient(colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7)))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .statusBarsPadding()
    ) {
        /* --- Top Header (Fix Height) --- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(purpleGradient, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Quiz Completed!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Surface(shape = CircleShape, color = Color.White.copy(0.2f), modifier = Modifier.size(70.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("🎉", fontSize = 35.sp) }
                }
            }
        }

        /* --- Score Section (Centered in the remaining space) --- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Ye bachi hui puri space le lega
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Content ko vertical center karega
        ) {
            Text("Your Final Score", fontSize = 18.sp, color = Color.Gray)

            // Score with extra emphasis
            Text(
                text = "$score / $totalQuestions",
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF9C27B0),
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(16.dp))

            // Accuracy Card
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Accuracy: $percentage%",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        /* --- Bottom Button Section --- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        ResultRepository.saveResult(userId, classroomId, score, totalQuestions)
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Back to Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}