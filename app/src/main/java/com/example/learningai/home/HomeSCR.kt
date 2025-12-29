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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningai.R
import com.example.learningai.model.SubjectUI
import com.example.learningai.nav.Routes


@Composable
fun HomeSCR(navController: NavController) {

    val subjects = listOf(
        SubjectUI("app_dev", "App Development", "Kotlin • Compose • MVVM", R.drawable.outline_question_mark_24),
        SubjectUI("web_dev", "Web Development", "HTML • CSS • React", R.drawable.outline_question_mark_24),
        SubjectUI("dsa", "DSA", "Arrays • Trees • Graphs", R.drawable.outline_question_mark_24),
        SubjectUI("ai_ml", "AI / ML", "Neural Nets • TensorFlow", R.drawable.outline_question_mark_24)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // 👋 Header
        Text(
            text = "👋 Hi user",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
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
