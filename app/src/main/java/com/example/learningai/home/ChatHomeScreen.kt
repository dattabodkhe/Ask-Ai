package com.example.learningai.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.model.chatDummyList
import com.example.learningai.ui.theme.CardDark
import com.example.learningai.ui.theme.DarkBlue
import com.example.learningai.ui.theme.appGradient

@Composable
fun ChatHomeScreen(
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatDummyList()) { chat ->
                ChatRow(
                    chat = chat,
                    onClick = {
                        // TODO: open classroom / chat screen
                    },
                    onShareClick = {
                        // TODO: share classroom code
                    }
                )
            }
        }
    }
}