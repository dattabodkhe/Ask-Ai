package com.example.learningai.nav

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import com.example.learningai.home.HomeSCR
import com.example.learningai.ui.nav.AppBottomBar
import com.example.learningai.user.HomeScreen
import com.example.learningai.user.UserInputSCR
import com.example.learningai.user.UserProfileSCR

@Composable
fun MainScreen() {

    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔹 TOP CONTENT AREA
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> UserInputSCR()
                2 -> UserProfileSCR()
            }
        }


        AppBottomBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}