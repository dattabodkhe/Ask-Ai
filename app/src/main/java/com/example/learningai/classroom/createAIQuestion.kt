package com.example.learningai.classroom

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.NavController
import com.example.learningai.model.Difficulty
import com.example.learningai.nav.Routes
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAIquestion(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var questionCount by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(Difficulty.EASY) }
    var isGenerating by remember { mutableStateOf(false) }

    var subjectError by remember { mutableStateOf(false) }
    var subjectErrorMessage by remember { mutableStateOf("") }
    var countError by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    fun validateForm(): Boolean {
        val input = selectedSubject.trim()
        val count = questionCount.toIntOrNull()
        val isValidText = input.matches(Regex("^[a-zA-Z\\s]{3,25}$"))
        val isSpam = input.length > 2 && input.all { it.lowercaseChar() == input[0].lowercaseChar() }

        var isAllValid = true
        if (input.isBlank() || !isValidText || isSpam) {
            subjectErrorMessage = "Invalid subject! Please use real words."
            subjectError = true
            isAllValid = false
        } else { subjectError = false }

        if (count == null || count !in 1..50) {
            countError = true
            isAllValid = false
        } else { countError = false }

        return isAllValid
    }

    // --- Navigation Dialog (Themed) ---
    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text("Questions Ready! 🤖", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("AI has generated the questions. What's the next step?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                Button(
                    onClick = {
                        showOptions = false
                        val dummyQuestions = listOf(
                            "What is $selectedSubject?",
                            "Explain the importance of $selectedSubject.",
                            "Describe a key concept in $selectedSubject."
                        )
                        val jsonString = Gson().toJson(dummyQuestions)
                        val encodedJson = Uri.encode(jsonString)
                        navController.navigate("${Routes.PREVIEW_QUESTIONS}/TEMP_ID/$encodedJson")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("Send in Classroom") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showOptions = false
                    val selfId = "SELF_${UUID.randomUUID()}"
                    navController.navigate("${Routes.QUESTIONSCREEN}/$selfId/${selectedSubject.trim()}/${questionCount.trim()}/${difficulty.name}")
                }) { Text("Self Practice", color = MaterialTheme.colorScheme.primary) }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    /* -------- UI LAYOUT -------- */
    val bgGradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            /* Header */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    "Create Powerful Questions 💡",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Push your limits. Let AI boost your learning 🚀",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Spacer(Modifier.height(32.dp))

            /* Card Content */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Generate AI Questions 🤖",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = { selectedSubject = it; if (subjectError) subjectError = false },
                        label = { Text("Subject Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        isError = subjectError,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        supportingText = { if (subjectError) Text(text = subjectErrorMessage) },
                        enabled = !isGenerating,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    OutlinedTextField(
                        value = questionCount,
                        onValueChange = { if (it.all { char -> char.isDigit() }) { questionCount = it; if (countError) countError = false } },
                        label = { Text("Questions (1-50)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        isError = countError,
                        supportingText = { if (countError) Text(text = "Enter 1-50") },
                        enabled = !isGenerating,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Text(
                        "Difficulty",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Difficulty.entries.forEach { level ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = difficulty == level,
                                    onClick = { if(!isGenerating) difficulty = level },
                                    enabled = !isGenerating,
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    level.name.lowercase().capitalize(),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    /* ACTION BUTTON */
                    Button(
                        onClick = {
                            if (validateForm()) {
                                isGenerating = true
                                scope.launch {
                                    delay(3000)
                                    isGenerating = false
                                    showOptions = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        ),
                        enabled = !isGenerating
                    ) {
                        if (isGenerating) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(12.dp))
                                Text("AI is thinking...")
                            }
                        } else {
                            Text("CREATE AI QUESTIONS", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }
}