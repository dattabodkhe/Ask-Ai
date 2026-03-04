package com.example.learningai.classroom

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/* --- 1. MESSAGE MODEL (Ye zaroori hai error hatane ke liye) --- */
data class Message(
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "User",
    val timestamp: Timestamp? = null,
    val type: String = "text"
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

    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var classroomName by remember { mutableStateOf("Classroom") }
    var inviteCode by remember { mutableStateOf("") }
    var memberCount by remember { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // FETCH CLASSROOM INFO
    LaunchedEffect(classId) {
        val doc = firestore.collection("classrooms").document(classId).get().await()
        if (doc.exists()) {
            classroomName = doc.getString("name") ?: "Classroom"
            inviteCode = doc.getString("inviteCode") ?: ""
            val members = doc.get("members") as? List<*>
            memberCount = members?.size ?: 0
        }
    }

    // 2. REAL-TIME MESSAGES LISTENER (Fixed with DisposableEffect)
    DisposableEffect(classId) {
        val query = firestore.collection("classrooms")
            .document(classId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null) {
                messages = snapshot.toObjects(Message::class.java)
            }
        }

        onDispose {
            registration.remove()
        }
    }

    // Auto-scroll logic
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { navController.navigate("group_profile/$classId") }
                    ) {
                        Text(classroomName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("$memberCount Members", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null) }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Invite Code: $inviteCode") }, onClick = {})
                        DropdownMenuItem(text = { Text("Share Link") }, onClick = { shareClassroom(context, classroomName, inviteCode) })
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier.padding(12.dp).navigationBarsPadding().imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                val msg = Message(
                                    text = messageText,
                                    senderId = currentUser?.uid ?: "",
                                    senderName = currentUser?.displayName ?: "User",
                                    timestamp = Timestamp.now(),
                                    type = "text"
                                )
                                firestore.collection("classrooms").document(classId)
                                    .collection("messages").add(msg)
                                messageText = ""
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(MaterialTheme.colorScheme.background)
        ) {
            AIInviteCard(onInviteClick = {
                navController.navigate(Routes.CREATE_AI_QUESTION)
            })

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(messages) { msg ->
                    val isMe = msg.senderId == currentUser?.uid
                    MessageBubble(msg, isMe, onJoinQuiz = {
                        navController.navigate("${Routes.QUESTIONSCREEN}/$classId/${msg.text}/10/MEDIUM")
                    })
                }
            }
        }
    }
}

/* --- COMPONENTS --- */

@Composable
fun MessageBubble(msg: Message, isMe: Boolean, onJoinQuiz: () -> Unit) {
    val isAiQuiz = msg.type == "ai_questions"

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        if (!isMe) {
            Text(msg.senderName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp, bottom = 2.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 0.dp,
                bottomEnd = if (isMe) 0.dp else 16.dp
            ),
            color = if (isAiQuiz) MaterialTheme.colorScheme.primaryContainer else (if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (isAiQuiz) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🤖", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Text("AI QUIZ CHALLENGE", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Subject: ${msg.text}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onJoinQuiz, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Text("START QUIZ", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(text = msg.text, color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
fun AIInviteCard(onInviteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onInviteClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = MaterialTheme.colorScheme.secondary) {
                Box(contentAlignment = Alignment.Center) { Text("✨", fontSize = 20.sp) }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Generate AI Quiz", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Post a challenge for everyone", style = MaterialTheme.typography.labelSmall)
            }
            Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/* SHARE FUNCTION (Ye missing tha code mein) */
fun shareClassroom(context: Context, name: String, code: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Join my Classroom '$name' using code: $code")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}