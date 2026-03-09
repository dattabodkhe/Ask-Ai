package com.example.learningai.premission

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.learningai.ai.AiRepository
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class ClassroomUI(
    val id: String,
    val name: String,
    val memberCount: Int,
    val subject: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectClassroomScreen(
    navController: NavController,
    subject: String,
    count: Int,
    difficulty: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var classrooms by remember { mutableStateOf<List<ClassroomUI>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSending by remember { mutableStateOf(false) }
    var selectedClassroom by remember { mutableStateOf<ClassroomUI?>(null) }

    val aiRepository = remember { AiRepository(context) }
    var questionsList by remember { mutableStateOf<List<String>>(emptyList()) }

    /* ---------------- FETCH CLASSROOMS ---------------- */
    LaunchedEffect(currentUser?.uid) {
        if (currentUser == null) {
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val snapshot = firestore.collection("classrooms")
                .whereArrayContains("members", currentUser.uid)
                .get()
                .await()

            classrooms = snapshot.documents.map { doc ->
                val membersList = doc.get("members") as? List<*> ?: emptyList<Any>()
                ClassroomUI(
                    id = doc.id,
                    name = doc.getString("name") ?: "Untitled Class",
                    memberCount = membersList.size,
                    subject = doc.getString("subject") ?: "AI Quiz"
                )
            }
        } catch (e: Exception) {
            Log.e("FIRESTORE", e.message ?: "Error")
        } finally {
            isLoading = false
        }
    }

    /* ---------------- GENERATE AI QUESTIONS ---------------- */
    LaunchedEffect(Unit) {
        try {
            val prompt = """
Generate exactly $count $difficulty level multiple choice questions for '$subject'.
Return only clean questions.
""".trimIndent()

            val response = aiRepository.chat(prompt)

            questionsList = response.split("\n")
                .filter { it.isNotBlank() }
                .map { it.replace(Regex("^\\d+\\.\\s*"), "").trim() }

        } catch (e: Exception) {
            Toast.makeText(context, "AI Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (selectedClassroom != null) {
                Surface(
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Button(
                        onClick = {
                            if (questionsList.isEmpty()) {
                                Toast.makeText(context, "Questions not ready yet", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isSending = true
                            scope.launch {
                                try {
                                    val messageData = hashMapOf(
                                        "senderId" to currentUser?.uid,
                                        "senderName" to (currentUser?.displayName ?: "User"),
                                        "questions" to questionsList,
                                        "text" to subject,
                                        "timestamp" to FieldValue.serverTimestamp(),
                                        "type" to "ai_questions",
                                        "difficulty" to difficulty,
                                        "questionCount" to count
                                    )

                                    withContext(Dispatchers.IO) {
                                        firestore.collection("classrooms")
                                            .document(selectedClassroom!!.id)
                                            .collection("messages")
                                            .add(messageData)
                                            .await()
                                    }

                                    Toast.makeText(context, "Quiz Shared! 🤖", Toast.LENGTH_SHORT).show()

                                    navController.navigate(Routes.HOME) {
                                        popUpTo(0)
                                    }

                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isSending = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(56.dp),
                        enabled = !isSending,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isSending) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text("Post to ${selectedClassroom!!.name}")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Text(
                        "Select Group",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(classrooms) { classroom ->
                        ClassroomCard(
                            classroom = classroom,
                            isSelected = selectedClassroom?.id == classroom.id
                        ) {
                            selectedClassroom = classroom
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassroomCard(
    classroom: ClassroomUI,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(0.4f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(0.3f)
        ),
        border = if (isSelected)
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onSelect)
            Column {
                Text(text = classroom.name, fontWeight = FontWeight.Bold)
                Text(text = "Subject: ${classroom.subject}", fontSize = 12.sp)
                Text(text = "${classroom.memberCount} Members", fontSize = 11.sp)
            }
        }
    }
}