package com.example.learningai.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Admin Panel 🔐", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Button(onClick = { }) {
            Text("Add Interview Question")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { }) {
            Text("Add Notes")
        }
    }
}