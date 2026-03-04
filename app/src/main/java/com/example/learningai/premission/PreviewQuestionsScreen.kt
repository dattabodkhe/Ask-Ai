package com.example.learningai.premission

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewQuestionsScreen(
    navController: NavController,
    initialQuestions: List<String>,
    selectedClassId: String
) {
    val questionsList = remember { initialQuestions.toMutableStateList() }
    var editingIndex by remember { mutableStateOf(-1) }
    var editText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            Surface(tonalElevation = 8.dp, shadowElevation = 8.dp, color = Color.White) {
                Button(
                    onClick = {
                        if (questionsList.isNotEmpty()) {
                            val updatedJson = Gson().toJson(questionsList.toList())
                            val encodedJson = Uri.encode(updatedJson)
                            navController.navigate("select_classroom/$encodedJson")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                ) {
                    Text("Select Group to Send", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB))) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .statusBarsPadding().padding(bottom = 24.dp, start = 8.dp, end = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Text("Review Questions", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f).padding(bottom = innerPadding.calculateBottomPadding()),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(questionsList) { index, question ->
                    QuestionEditCard(
                        index = index + 1,
                        text = question,
                        onDelete = { questionsList.removeAt(index) },
                        onEdit = { editingIndex = index; editText = question }
                    )
                }
            }
        }

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
                        questionsList[editingIndex] = editText
                        editingIndex = -1
                    }) { Text("Update") }
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = "$index.",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.width(28.dp)
                )
                Text(text = text, color = Color(0xFF2D3436), modifier = Modifier.weight(1f))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onEdit) { Text("Edit", color = Color(0xFF4F46E5)) }
                TextButton(onClick = onDelete) { Text("Delete", color = Color.Red) }
            }
        }
    }
}