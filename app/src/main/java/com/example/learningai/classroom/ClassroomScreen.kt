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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.learningai.model.Contact
import com.example.learningai.model.Message
import com.example.learningai.nav.Routes
import com.example.learningai.premission.getContacts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- Data Model for UI ---
data class ClassroomUI(val id: String, val name: String)

/* =========================================================
   1. MAIN CLASSROOM LIST SCREEN
   ========================================================= */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var classroomList by remember { mutableStateOf<List<ClassroomUI>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            firestore.collection("classrooms")
                .whereArrayContains("members", currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("FIRESTORE", "Listen failed.", error)
                        isLoading = false
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        classroomList = snapshot.documents.map { doc ->
                            ClassroomUI(doc.id, doc.getString("name") ?: "Unnamed Group")
                        }
                    }
                    isLoading = false
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                        .statusBarsPadding()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                "Your Classrooms",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Manage & collaborate easily 🚀",
                                color = Color.White.copy(0.8f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            if (isLoading) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else if (classroomList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No Classrooms Joined yet 🔍",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = { navController.navigate(Routes.CREATE_CLASSROOM) }) {
                            Text("Create One Now")
                        }
                    }
                }
            } else {
                items(classroomList) { classroom ->
                    ModernClassroomCard(title = classroom.name) {
                        navController.navigate("classroom_chat/${classroom.id}")
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {
                navController.navigate("create_classroom")
            }
        ) {
            Icon(Icons.Default.Add, null)
        }
    }
}

/* =========================================================
   2. MESSAGE BUBBLE (Logic Updated to handle AI Questions)
   ========================================================= */
@Composable
fun MessageBubble(msg: Message, isMe: Boolean, onJoinQuiz: (List<String>) -> Unit) {
    val isAiQuiz = msg.type == "ai_questions"

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        if (!isMe) {
            Text(
                msg.senderName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 0.dp,
                bottomEnd = if (isMe) 0.dp else 16.dp
            ),
            color = if (isAiQuiz) MaterialTheme.colorScheme.primaryContainer
            else (if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
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

                    // Asli Subject Name yahan dikhega
                    Text("Subject: ${msg.text}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    // Asli Question count yahan dikhega
                    Text("Questions: ${msg.questionCount}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { onJoinQuiz(msg.questionList) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("START QUIZ", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = msg.text,
                        color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/* =========================================================
   3. MODERN CLASSROOM CARD
   ========================================================= */
@Composable
fun ModernClassroomCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(18.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("Tap to open discussion", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/* =========================================================
   4. CREATE CLASSROOM SCREEN
   ========================================================= */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateClassroomSCR(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    // States
    var classroomName by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var showFriendSheet by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var selectedFriends by remember { mutableStateOf(setOf<String>()) }
    var permissionGranted by remember { mutableStateOf(false) }

    // Contact Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> permissionGranted = granted }

    // Check Permission & Fetch Contacts
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            contacts = withContext(Dispatchers.IO) { getContacts(context) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background // Automatic Dark/Light support
    ) { innerPadding ->
        // innerPadding handles status bar/navigation bar spacing automatically
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // --- 1. FIXED GRADIENT HEADER ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(0.7f))
                            ),
                            shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
                        )
                        .statusBarsPadding() // Header camera notch ke niche rahega
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("Create Classroom", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Build your own learning space 🚀", color = Color.White.copy(0.85f), fontSize = 15.sp, modifier = Modifier.padding(start = 12.dp))
                    }
                }
            }

            // --- 2. INPUT FIELDS ---
            item {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    OutlinedTextField(
                        value = classroomName,
                        onValueChange = { classroomName = it },
                        label = { Text("Classroom Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    // Member Selection Button
                    OutlinedButton(
                        onClick = { showFriendSheet = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(15.dp)
                    ) {
                        Icon(Icons.Default.Person, null)
                        Spacer(Modifier.width(10.dp))
                        Text("Add Members from Contacts")
                    }

                    // Display Selected Members
                    if (selectedFriends.isNotEmpty()) {
                        Text("Selected (${selectedFriends.size})", fontWeight = FontWeight.SemiBold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedFriends.forEach { number ->
                                AssistChip(
                                    onClick = { selectedFriends = selectedFriends - number },
                                    label = { Text(number) },
                                    trailingIcon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp)) }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // --- 3. CREATE BUTTON ---
                    Button(
                        onClick = {
                            if (classroomName.isBlank() || subject.isBlank() || currentUser == null) {
                                Toast.makeText(context, "Fill all details", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            val classroomId = firestore.collection("classrooms").document().id
                            val inviteCode = classroomId.take(6).uppercase()

                            val classroomData = hashMapOf(
                                "name" to classroomName.trim(),
                                "subject" to subject.trim(),
                                "inviteCode" to inviteCode,
                                "createdBy" to currentUser.uid,
                                "members" to listOf(currentUser.uid), // Adding creator as first member
                                "createdAt" to FieldValue.serverTimestamp()
                            )

                            firestore.collection("classrooms").document(classroomId)
                                .set(classroomData)
                                .addOnSuccessListener {
                                    isLoading = false
                                    // Navigation to chat
                                    navController.navigate("classroom_chat/$classroomId") {
                                        popUpTo("create_classroom") { inclusive = true }
                                    }
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Create Classroom", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // --- 4. CONTACTS BOTTOM SHEET ---
    if (showFriendSheet) {
        ModalBottomSheet(onDismissRequest = { showFriendSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Text("Select Friends", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(contacts) { contact ->
                        ListItem(
                            headlineContent = { Text(contact.name) },
                            supportingContent = { Text(contact.number) },
                            leadingContent = {
                                Checkbox(
                                    checked = selectedFriends.contains(contact.number),
                                    onCheckedChange = {
                                        selectedFriends = if (it == true) selectedFriends + contact.number
                                        else selectedFriends - contact.number
                                    }
                                )
                            },
                            modifier = Modifier.clickable {
                                selectedFriends = if (selectedFriends.contains(contact.number))
                                    selectedFriends - contact.number else selectedFriends + contact.number
                            }
                        )
                    }
                }
                Button(
                    onClick = { showFriendSheet = false },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                ) { Text("Done") }
            }
        }
    }
}
@Composable
fun GradientHeader(title: String, subtitle: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // 1. Sabse important: statusBarsPadding() add kiya hai
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(0.8f)
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
            )
            .statusBarsPadding() // Ye line header ko status bar ke niche rakhegi
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(32.dp) // Size thoda optimize kiya
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = subtitle,
                color = Color.White.copy(0.85f),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 44.dp) // Text ko icon ke sath align kiya
            )
        }
    }
}