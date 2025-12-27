package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learningai.admin.AdminViewModel
import com.example.learningai.nav.Routes
import com.example.learningai.viewmodel.InterviewViewModel

@Composable
fun UserProfileSCR(
    navController: NavController,
    interviewViewModel: InterviewViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel()
) {

    val uiState by interviewViewModel.uiState.collectAsState()
    val isAdmin by adminViewModel.isAdmin.collectAsState()

    var tapCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        adminViewModel.checkAdmin()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔐 ADMIN HIDDEN ENTRY
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

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("DB", color = Color.White, fontSize = 36.sp)
        }

        Spacer(Modifier.height(24.dp))

        Text("Interview Stats 📊", fontWeight = FontWeight.Bold)

        StatCard("Attempted", uiState.attemptedCount)
        StatCard("Correct", uiState.correctCount)
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