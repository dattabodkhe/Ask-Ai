package com.example.learningai.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.R
import com.example.learningai.model.SubjectUI
import com.example.learningai.nav.Routes

@Composable
fun HomeSCR(navController: NavController) {

    val subjects = listOf(
        SubjectUI(
            id = "app_dev",
            title = "App Development",
            subtitle = "Kotlin, Jetpack Compose, MVVM",
            icon = R.drawable.outline_question_mark_24
        ),
        SubjectUI(
            id = "web_dev",
            title = "Web Development",
            subtitle = "HTML, CSS, JavaScript, React",
            icon = R.drawable.outline_question_mark_24
        ),
        SubjectUI(
            id = "dsa",
            title = "DSA",
            subtitle = "Arrays, Linked List, Trees",
            icon = R.drawable.outline_question_mark_24
        ),
        SubjectUI(
            id = "ai_ml",
            title = "AI / ML",
            subtitle = "TensorFlow, PyTorch, Neural Nets",
            icon = R.drawable.outline_question_mark_24
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "👋 Hi Datta",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "What do you want to learn today?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(subjects) { subject ->
                SubjectCard(
                    subject = subject,

                    onInterviewClick = {
                        // ✅ FIXED
                        navController.navigate("${Routes.INTERVIEW}/${subject.id}")
                    },

                    onNotesClick = {
                        navController.navigate("${Routes.NOTES}/${subject.id}")
                    }
                )
            }
        }
    }
}