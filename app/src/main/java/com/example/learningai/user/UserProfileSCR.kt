package com.example.learningai.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learningai.Admin.AdminViewModel
import com.example.learningai.nav.Routes

@Composable
fun UserProfileSCR(
    navController: NavController,
    interviewViewModel: InterviewViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel()
) {
    val uiState by interviewViewModel.uiState.collectAsState()
    val isAdmin by adminViewModel.isAdmin.collectAsState()

    var tapCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        adminViewModel.checkAdmin()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {


        Text(
            text = "Your Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                tapCount++
                if (tapCount == 5 && isAdmin) {
                    navController.navigate(Routes.ADMIN)
                    tapCount = 0
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Track your learning progress",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))


        StatCard(
            title = "Questions Attempted",
            value = uiState.attemptedCount
        )

        StatCard(
            title = "Correct Answers",
            value = uiState.correctCount
        )

        val accuracy =
            if (uiState.attemptedCount == 0) 0
            else (uiState.correctCount * 100 / uiState.attemptedCount)

        StatCard(
            title = "Accuracy",
            value = "$accuracy%"
        )
    }
}
@Composable
fun StatCard(
    title: String,
    value: Any
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = value.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}