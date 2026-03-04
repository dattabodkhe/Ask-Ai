package com.example.learningai.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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

    // Dynamic Gradient based on Theme
    val mainGradient = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fix 1: Adaptive Background
            .statusBarsPadding()
    ) {
        /* --- Top Header --- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(mainGradient, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Quiz Completed!",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(0.2f),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎉", fontSize = 40.sp)
                    }
                }
            }
        }

        /* --- Score Section --- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Your Final Score",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Score with Theme Primary Color
            Text(
                text = "$score / $totalQuestions",
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary, // Glows in dark mode
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(24.dp))

            // Accuracy Card - Adaptive Colors
            Surface(
                color = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.primaryContainer.copy(0.2f)
                else Color(0xFFE8F5E9),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Accuracy: $percentage%",
                        color = if (isSystemInDarkTheme())
                            MaterialTheme.colorScheme.primary
                        else Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        /* --- Bottom Button Section --- */
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 2.dp // Subtle lift
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
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Back to Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}