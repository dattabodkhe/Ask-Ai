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
        SubjectUI("app_dev", "App Development", "Kotlin • Compose", R.drawable.outline_question_mark_24),
        SubjectUI("web_dev", "Web Development", "HTML • React", R.drawable.outline_question_mark_24),
        SubjectUI("dsa", "DSA", "Arrays • Trees", R.drawable.outline_question_mark_24)
    )
    MainScaffold(
        title = "Learning AI",
        navController = navController
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    "Hi Datta 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Choose what you want to learn",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(subjects) { subject ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(subject.title, fontWeight = FontWeight.Bold)
                        Text(subject.subtitle, style = MaterialTheme.typography.bodySmall)

                        Spacer(Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("${Routes.INTERVIEW}/${subject.id}")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Interview")
                            }

                            OutlinedButton(
                                onClick = {
                                    navController.navigate("${Routes.NOTES}/${subject.id}")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Notes")
                            }
                        }
                    }
                }
            }
        }
    }
}
