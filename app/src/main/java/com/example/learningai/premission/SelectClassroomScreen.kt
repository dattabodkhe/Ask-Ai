package com.example.learningai.premission

import android.net.Uri
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
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ClassroomUI(val id: String, val name: String, val memberCount: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectClassroomScreen(
    navController: NavController,
    questionsJson: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var classrooms by remember { mutableStateOf<List<ClassroomUI>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSending by remember { mutableStateOf(false) }
    var selectedClassId by remember { mutableStateOf<String?>(null) }

    // No showInitialPopup state anymore!

    val questionsList = remember(questionsJson) {
        try {
            Gson().fromJson(questionsJson, Array<String>::class.java).toList()
        } catch (e: Exception) { emptyList<String>() }
    }

    LaunchedEffect(currentUser?.uid) {
        if (currentUser == null) { isLoading = false; return@LaunchedEffect }
        try {
            val snapshot = firestore.collection("classrooms")
                .whereArrayContains("members", currentUser.uid).get().await()
            classrooms = snapshot.documents.map { doc ->
                val membersList = doc.get("members") as? List<*> ?: emptyList<Any>()
                ClassroomUI(doc.id, doc.getString("name") ?: "Untitled Class", membersList.size)
            }
        } catch (e: Exception) { Log.e("FIRESTORE", "${e.message}") }
        finally { isLoading = false }
    }

    Scaffold(
        bottomBar = {
            // Button tabhi dikhega jab user list se koi classroom select kar lega
            if (selectedClassId != null) {
                Surface(tonalElevation = 8.dp, shadowElevation = 8.dp, color = Color.White) {
                    Button(
                        onClick = {
                            isSending = true
                            scope.launch {
                                try {
                                    val messageData = hashMapOf(
                                        "senderId" to currentUser?.uid,
                                        "questions" to questionsList,
                                        "timestamp" to FieldValue.serverTimestamp(),
                                        "type" to "ai_questions"
                                    )
                                    firestore.collection("classrooms")
                                        .document(selectedClassId!!)
                                        .collection("messages")
                                        .add(messageData)
                                        .await()

                                    Toast.makeText(context, "Questions Sent Successfully! ✅", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(0) // Pura backstack clear karke home par jao
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally { isSending = false }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                        enabled = !isSending,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                    ) {
                        if (isSending) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Confirm & Send to Group", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB)).padding(innerPadding)) {
            /* Header Section */
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .statusBarsPadding().padding(bottom = 24.dp, start = 8.dp, end = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Text("Select Target Group", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4F46E5))
                }
            } else if (classrooms.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No classrooms found. Join one first!", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(classrooms) { classroom ->
                        ClassroomCard(
                            classroom = classroom,
                            isSelected = selectedClassId == classroom.id
                        ) {
                            selectedClassId = classroom.id
                        }
                    }
                }
            }
        }
    }
}

// ClassroomCard component same rahega...

@Composable
fun ClassroomCard(classroom: ClassroomUI, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFEEF2FF) else Color.White),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF4F46E5)) else null,
        elevation = CardDefaults.cardElevation(if (isSelected) 0.dp else 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isSelected, onClick = { onSelect() }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4F46E5)))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = classroom.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436))
                Text(text = "${classroom.memberCount} Members", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}