package com.example.learningai.Admin


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AdminDashboardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Admin Panel ",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("add_question") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Interview Question")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("add_notes") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Notes")
        }
    }
}