package com.example.learningai.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.model.Questions
import com.example.learningai.nav.Routes

@Composable

fun HomeSCR(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Welcome 👋",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        InterviewOptionButton(
            title = "Interview Questions",
            subtitle = "MCQ based questions",
            onClick = {
                navController.navigate(Routes.INTERVIEW)
            }
        )

        InterviewOptionButton(
            title = "Practice Test",
            subtitle = "Timed practice tests",
            onClick = { }
        )

        InterviewOptionButton(
            title = "Notes",
            subtitle = "Short revision notes",
            onClick = { }
        )
    }
}