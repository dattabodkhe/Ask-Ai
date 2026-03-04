package com.example.learningai.premission

import android.net.Uri
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewQuestionsScreen(
    navController: NavController,
    selectedClassId: String,
    initialQuestions: String
) {
    // 1. FIX: String JSON ko pehle List mein convert karna zaroori hai
    val decodedQuestions = remember(initialQuestions) {
        val type = object : TypeToken<List<String>>() {}.type
        val list: List<String> = Gson().fromJson(initialQuestions, type) ?: emptyList()
        list.toMutableStateList()
    }

    var editingIndex by remember { mutableIntStateOf(-1) }
    var editText by remember { mutableStateOf("") }

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
                            val updatedJson = Gson().toJson(decodedQuestions.toList())
                            val encodedJson = Uri.encode(updatedJson)

                            // Logic based on classId
                            if (selectedClassId.isNotEmpty() && !selectedClassId.contains("TEMP_ID")) {
                                // Yahan apna message send karne ka logic likhein
                            } else {
                                navController.navigate("select_classroom/$encodedJson")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Select Group to Send", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                        Text("Review Questions", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("${decodedQuestions.size} AI Generated Questions", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    }
                }
            }

            /* --- QUESTIONS LIST --- */
            if (decodedQuestions.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No questions to review", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (editText.isNotBlank()) {
                            decodedQuestions[editingIndex] = editText
                            editingIndex = -1
                        }
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { editingIndex = -1 }) { Text("Cancel") }
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text.take(0), color = Color.White) // Dummy
                        Text(text = index.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(text = text, modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
}