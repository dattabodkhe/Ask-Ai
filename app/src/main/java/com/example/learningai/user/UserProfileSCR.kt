package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.viewmodel.InterviewViewModel

@Composable
@Preview(showSystemUi = true)
fun UserProfileSCR(
    viewModel: InterviewViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val accuracy = if (uiState.attemptedCount > 0) {
        (uiState.correctCount * 100) / uiState.attemptedCount
    } else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 Profile Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "DB",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Profile Info
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Datta Bodkhe",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Android Developer",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Learning Kotlin & Jetpack Compose",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 INTERVIEW STATS SECTION
        Text(
            text = "Interview Stats 📊",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatCard(title = "Attempted Questions", value = uiState.attemptedCount)
        StatCard(title = "Correct Answers", value = uiState.correctCount)
        StatCard(title = "Accuracy", value = "$accuracy %")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Edit later */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Edit Profile")
        }
    }
}







@Composable
fun StatCard(title: String, value: Any) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(text = value.toString(), fontWeight = FontWeight.Bold)
        }
    }
}