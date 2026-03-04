package com.example.learningai.classroom

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.learningai.model.Contact
import com.example.learningai.nav.Routes
import com.example.learningai.premission.getContacts
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/* --- MESSAGE MODEL --- */
data class Message(
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "User",
    val timestamp: Timestamp? = null,
    val type: String = "text",
    val questions: List<String> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomChatScreen(
    navController: NavController,
    classId: String
) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    // --- STATES ---
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var isMember by remember { mutableStateOf(false) }
    var classroomName by remember { mutableStateOf("Classroom") }
    var inviteCode by remember { mutableStateOf("") } // New State for Invite Code
    var memberCount by remember { mutableStateOf(0) }
    var listener by remember { mutableStateOf<ListenerRegistration?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showAddFriendSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // --- DATA FETCHING ---
    LaunchedEffect(classId) {
        try {
            val doc = firestore.collection("classrooms").document(classId).get().await()
            if (doc.exists()) {
                classroomName = doc.getString("name") ?: "Classroom"
                inviteCode = doc.getString("inviteCode") ?: "" // Fetching Invite Code
                val members = doc.get("members") as? List<*>
                memberCount = members?.size ?: 0
                isMember = members?.contains(currentUser?.uid) == true
            }
        } catch (e: Exception) {
            isMember = false
        }
    }

    // Auto-scroll logic same rahega...

    /* ================= UI LAYOUT ================= */
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Column(modifier = Modifier.clickable { navController.navigate("group_profile/$classId") }) {
                            Text(classroomName, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("$memberCount Members", color = Color.White.copy(0.8f), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, null, tint = Color.White)
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Add Friend in Group") },
                                onClick = {
                                    showMenu = false
                                    // Permission logic...
                                }
                            )
                            // SHARE FEATURE WORKING NOW
                            DropdownMenuItem(
                                text = { Text("Share Classroom Link") },
                                onClick = {
                                    showMenu = false
                                    // Yahan hum inviteCode bhej rahe hain
                                    shareClassroom(context, classroomName, inviteCode)
                                }
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F7FB))
                .padding(innerPadding)
        ) {
            AIInviteCard(onInviteClick = {
                navController.navigate("create_ai_question")
            })

            LazyColumn(
                state = listState, // ATTACHED listState
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
            ) {
                items(messages) { msg ->
                    val isMe = msg.senderId == currentUser?.uid
                    MessageBubble(msg, isMe, onJoinQuiz = {
                        navController.navigate("${Routes.QUESTIONSCREEN}/$classId/${msg.text.ifBlank { "Quiz" }}/10/MEDIUM")
                    })
                }
            }
        }
        // ... (Baaki ModalBottomSheet logic same hai) ...
    }
}

/* ================= COMPONENTS ================= */

@Composable
fun MessageBubble(msg: Message, isMe: Boolean, onJoinQuiz: () -> Unit) {
    val isAiQuiz = msg.type == "ai_questions"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp, topEnd = 18.dp,
                bottomStart = if (isMe) 18.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 18.dp
            ),
            color = if (isAiQuiz) Color(0xFFEEF2FF) else (if (isMe) Color(0xFF6C5CE7) else Color.White),
            tonalElevation = 2.dp,
            shadowElevation = 3.dp,
            border = if (isAiQuiz) BorderStroke(1.dp, Color(0xFF4F46E5)) else null
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (isAiQuiz) {
                    Text("🤖 AI QUIZ CHALLENGE", fontWeight = FontWeight.ExtraBold, color = Color(0xFF4F46E5), fontSize = 13.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Subject: ${msg.text.ifBlank { "General AI Quiz" }}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onJoinQuiz,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                    ) {
                        Text("JOIN QUIZ", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(text = msg.text, color = if (isMe) Color.White else Color.Black)
                }
            }
        }
    }
}

@Composable
fun AIInviteCard(onInviteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onInviteClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(Color(0xFFEEF2FF), CircleShape),
                contentAlignment = Alignment.Center
            ) { Text("🤖", fontSize = 24.sp) }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("AI Quiz Challenge 💡", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Generate quiz for this group", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Button(onClick = onInviteClick, shape = RoundedCornerShape(12.dp)) {
                Text("Create", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AddFriendSheetContent(
    contacts: List<Contact>,
    selected: Set<String>,
    onToggle: (String) -> Unit,
    onDone: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text("Select Friends", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(contacts) { contact ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onToggle(contact.number) }.padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = selected.contains(contact.number), onCheckedChange = null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(contact.name, fontWeight = FontWeight.Bold)
                        Text(contact.number, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Add Selected to Group")
        }
    }
}
fun shareClassroom(context: android.content.Context, name: String, id: String) {
    val sendIntent: android.content.Intent = android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        putExtra(android.content.Intent.EXTRA_TEXT, "Join my Classroom '$name' on LearningAI! \nLink: https://learningai.app/join/$id")
        type = "text/plain"
    }
    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}