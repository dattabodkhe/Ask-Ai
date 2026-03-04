package com.example.learningai.classroom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learningai.MVVM.QuestionsViewModel
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.nav.Routes
import kotlinx.coroutines.delay

@Composable
fun QuestionsScreen(
    navController: NavController,
    classroomId: String,
    subject: String,
    count: Int,
    difficulty: String
) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).questionDao() }
    val viewModel: QuestionsViewModel = viewModel(factory = QuestionsViewModelFactory(context))
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(classroomId, subject, count) {
        viewModel.loadOrGenerateQuestions(classroomId, subject, count, dao)
    }

    var timeLeft by remember { mutableIntStateOf(count * 60) }
    LaunchedEffect(state.questions.isNotEmpty()) {
        if (state.questions.isNotEmpty()) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
        }
    }

    val timeString = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)

    if (state.isLoading && state.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF9C27B0))
        }
        return
    }

    if (state.questions.isEmpty()) return
    val question = state.questions[state.index]
    val purpleGradient = Brush.verticalGradient(colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7)))

    Scaffold(
        containerColor = Color(0xFFF3F4F6),
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                Button(
                    onClick = {
                        if (state.index < state.questions.lastIndex) {
                            viewModel.nextQuestion()
                        } else {
                            // Navigate to Result Screen
                            navController.navigate("${Routes.RESULT}/$classroomId/${state.score}/${state.questions.size}")
                        }
                    },
                    enabled = state.selected != -1,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.selected != -1) Color(0xFF673AB7) else Color(0xFFD1D5DB)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (state.index == state.questions.lastIndex) "Finish Test" else "Next Question",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(purpleGradient).padding(20.dp)) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("$subject Quiz", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Surface(color = Color.White.copy(0.2f), shape = CircleShape) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Notifications, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(timeString, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    LinearProgressIndicator(
                        progress = { (state.index + 1f) / state.questions.size },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color.White, trackColor = Color.White.copy(0.3f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Question ${state.index + 1} of ${state.questions.size}", color = Color.White.copy(0.8f))
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                Text(text = question.question, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                Spacer(Modifier.height(32.dp))

                val options = listOf(question.optionA, question.optionB, question.optionC, question.optionD)
                options.forEachIndexed { index, option ->
                    QuizOptionItem(text = option, isSelected = state.selected == index, onClick = { viewModel.selectOption(index) })
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun QuizOptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, if (isSelected) Color(0xFF9C27B0) else Color(0xFFE5E7EB)),
        color = if (isSelected) Color(0xFFF3E5F5) else Color.White
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(24.dp).background(if (isSelected) Color(0xFF9C27B0) else Color.Transparent, CircleShape)
                    .border(2.dp, if (isSelected) Color(0xFF9C27B0) else Color(0xFFD1D5DB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(16.dp))
            Text(text = text, fontSize = 16.sp, color = if (isSelected) Color(0xFF9C27B0) else Color(0xFF4B5563), fontWeight = FontWeight.Medium)
        }
    }
}