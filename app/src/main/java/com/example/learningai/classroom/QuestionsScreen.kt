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
import androidx.compose.ui.draw.clip
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
import com.google.firebase.auth.FirebaseAuth
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

    // 1. Load Questions
    LaunchedEffect(classroomId, subject, count) {
        viewModel.loadOrGenerateQuestions(classroomId, subject, count, dao)
    }

    // 2. Timer Logic
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

    // Loading State
    if (state.isLoading && state.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(16.dp))
                Text("AI is generating your quiz...", color = MaterialTheme.colorScheme.primary)
            }
        }
        return
    }

    if (state.questions.isEmpty()) return
    val question = state.questions[state.index]
    val quizGradient = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary))

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(tonalElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp).navigationBarsPadding()) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                    Button(
                        onClick = {
                            // Final Score update sirf Next click par logic (Optional, aapka purana logic bhi chalega)
                            if (state.index < state.questions.lastIndex) {
                                viewModel.nextQuestion()
                            } else {
                                navController.navigate(
                                    Routes.resultRoute(classroomId, state.score, state.questions.size, userId)
                                ) { popUpTo(Routes.QUESTIONSCREEN) { inclusive = true } }
                            }
                        },
                        enabled = state.selected != -1, // Jab tak kuch select na ho, button band rahega
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (state.index == state.questions.lastIndex) "Finish Test" else "Next Question",
                            fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            /* --- Header --- */
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(quizGradient).padding(20.dp)) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(subject, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Surface(color = Color.White.copy(0.2f), shape = RoundedCornerShape(8.dp)) {
                            Text(timeString, color = Color.White, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    LinearProgressIndicator(
                        progress = { (state.index + 1f) / state.questions.size },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = Color.White, trackColor = Color.White.copy(0.3f)
                    )
                }
            }

            /* --- Question & Reselectable Options --- */
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text(text = question.question, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(24.dp))

                val options = listOf(question.optionA, question.optionB, question.optionC, question.optionD)

                options.forEachIndexed { index, option ->
                    // Selection state check
                    val isSelected = state.selected == index

                    QuizOptionItem(
                        text = option,
                        isSelected = isSelected,
                        onClick = {
                            viewModel.selectOption(index)
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
@Composable
fun QuizOptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        color = containerColor,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        CircleShape
                    )
                    .border(2.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(14.dp))
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}