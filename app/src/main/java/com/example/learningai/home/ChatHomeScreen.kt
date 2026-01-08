package com.example.learningai.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.learningai.model.chatDummyList
import com.example.learningai.nav.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHomeScreen(
    navController: NavController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "LearningAI",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    // ➕ Create Classroom
                    IconButton(onClick = {
                        navController.navigate(Routes.CREATE_CLASSROOM)
                    }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Create Classroom"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(chatDummyList()) { chat ->

                val classroomCode =
                    chat.classroomId.take(6).uppercase()

                ChatRow(
                    chat = chat,
                    onClick = {
                        // TODO: open QuestionsScreen
                    },
                    onShareClick = {
                        val classroomCode = chat.classroomId.take(6).uppercase()
                        shareClassroom(context, classroomCode)
                    }
                )
            }
        }
    }
}