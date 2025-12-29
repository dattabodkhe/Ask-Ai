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
import com.example.learningai.MVVM.InterviewViewModel

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

    Column(Modifier.padding(16.dp)) {

        Text(
            text = "Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                tapCount++
                if (tapCount == 5 && isAdmin) {
                    navController.navigate(Routes.ADMIN)
                    tapCount = 0
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        Text("Attempted: ${uiState.attemptedCount}")
        Text("Correct: ${uiState.correctCount}")
    }
}

@Composable
fun StatCard(title: String, value: Any) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title)
            Text(value.toString(), fontWeight = FontWeight.Bold)
        }
    }
}