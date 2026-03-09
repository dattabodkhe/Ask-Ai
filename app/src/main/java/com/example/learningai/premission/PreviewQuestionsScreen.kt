package com.example.learningai.premission

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewQuestionsScreen(
    navController: NavController,
    selectedClassId: String,
    subjectName: String,
    count: Int,
    difficulty: String
) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val aiRepository = remember { com.example.learningai.ai.AiRepository(context) }

    val decodedQuestions = remember { mutableStateListOf<String>() }

    var editingIndex by remember { mutableIntStateOf(-1) }
    var editText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var isGenerating by remember { mutableStateOf(true) } // AI loading state

    LaunchedEffect(Unit) {
        try {
            isGenerating = true
            val prompt = """
    Generate exactly $count $difficulty level multiple choice questions for '$subjectName'.
    Return ONLY a JSON array with this structure:
    [{"question": "text", "options": ["A", "B", "C", "D"], "correctIndex": 0}]
""".trimIndent()

            val response = aiRepository.chat(prompt)
            val questions = response.split("\n")
                .filter { it.isNotBlank() }
                .map { it.replace(Regex("^\\d+\\.\\s*"), "").trim() }

            decodedQuestions.clear()
            decodedQuestions.addAll(questions)
        } catch (e: Exception) {
            Toast.makeText(context, "AI Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isGenerating = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Button(
                    onClick = {
                        if (decodedQuestions.isNotEmpty()) {

                            // 👇 CASE 1: From CreateAIquestion (TEMP_ID)
                            if (selectedClassId.contains("TEMP_ID")) {

                                val encodedSubject = Uri.encode(subjectName)

                                navController.navigate(
                                    "${Routes.SELECT_CLASSROOM}/$encodedSubject/$count/$difficulty"
                                )

                            } else {
                                // 👇 CASE 2: Already inside a classroom
                                isSending = true

                                val aiMessage = mapOf(
                                    "text" to subjectName,
                                    "senderId" to (auth.currentUser?.uid ?: ""),
                                    "senderName" to (auth.currentUser?.displayName ?: "Instructor"),
                                    "timestamp" to com.google.firebase.Timestamp.now(),
                                    "type" to "ai_questions",
                                    "questionList" to decodedQuestions.toList(),
                                    "questionCount" to decodedQuestions.size,
                                    "difficulty" to difficulty
                                )

                                firestore.collection("classrooms")
                                    .document(selectedClassId)
                                    .collection("messages")
                                    .add(aiMessage)
                                    .addOnSuccessListener {
                                        isSending = false
                                        navController.popBackStack()
                                        Toast.makeText(context, "Quiz Sent to Classroom! 🚀", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        isSending = false
                                        Toast.makeText(context, "Failed to send", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = decodedQuestions.isNotEmpty() && !isSending && !isGenerating
                ) {
                    if (isSending) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Send to Classroom", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            /* --- HEADER --- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .statusBarsPadding()
                    .padding(bottom = 24.dp, top = 8.dp, start = 8.dp, end = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text("Review AI Questions", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(if(isGenerating) "Generating..." else "${decodedQuestions.size} Questions Ready", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    }
                }
            }

            /* --- QUESTIONS LIST / LOADING --- */
            if (isGenerating) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("AI is thinking...", color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else if (decodedQuestions.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No questions found. Please try again.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    itemsIndexed(decodedQuestions) { index, question ->
                        QuestionEditCard(
                            index = index + 1,
                            text = question,
                            onDelete = { decodedQuestions.removeAt(index) },
                            onEdit = {
                                editingIndex = index
                                editText = question
                            }
                        )
                    }
                }
            }
        }

        /* --- EDIT DIALOG --- */
        if (editingIndex != -1) {
            AlertDialog(
                onDismissRequest = { editingIndex = -1 },
                title = { Text("Edit Question") },
                text = {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (editText.isNotBlank()) {
                            decodedQuestions[editingIndex] = editText
                            editingIndex = -1
                        }
                    }) { Text("Save Changes") }
                },
                dismissButton = {
                    TextButton(onClick = { editingIndex = -1 }) { Text("Discard") }
                }
            )
        }
    }
}
@Composable
fun QuestionEditCard(index: Int, text: String, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // Question Number Circle
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = index.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // Question Text
                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Action Buttons (Edit & Delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}