package com.example.learningai.user



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningai.ui.nav.AppBottomBar

@Composable
fun HomeScreen() {

    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔹 TOP CONTENT (HOME UI)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // 🔹 BOTTOM BAR
        AppBottomBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}