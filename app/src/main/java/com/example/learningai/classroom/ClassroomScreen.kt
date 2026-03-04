package com.example.learningai.classroom

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.learningai.nav.Routes
import com.example.learningai.premission.getContacts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Real classrooms store karne ke liye state
    var classroomList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) } // Pair(ID, Name)
    var isLoading by remember { mutableStateOf(true) }

    // Firestore se data fetch karna
    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            firestore.collection("classrooms")
                .whereArrayContains("members", currentUser.uid) // Sirf wo groups jisme user member hai
                .addSnapshotListener { snapshot, error ->
                    if (snapshot != null) {
                        classroomList = snapshot.documents.map { doc ->
                            doc.id to (doc.getString("name") ?: "Unnamed Group")
                        }
                    }
                    isLoading = false
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            /* ---------- HEADER WITH BACK ARROW ---------- */
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                        .statusBarsPadding() // Status bar ke niche se shuru hoga
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // BACK BUTTON
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

            /* ---------- REAL CLASSROOM LIST ---------- */
            item { Spacer(Modifier.height(20.dp)) }

            if (isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(50.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF4F46E5))
                    }
                }
            } else if (classroomList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No Classrooms Joined yet 🔍", color = Color.Gray)
                        TextButton(onClick = { navController.navigate(Routes.JOIN_CLASSROOM) }) {
                            Text("Join or Create One")
                        }
                    }
                }
            } else {
                items(classroomList) { (id, name) ->
                    ModernClassroomCard(
                        title = name
                    ) {
                        // Ab hum Real Document ID pass karenge chat screen ko
                        navController.navigate("classroom_chat/$id")
                    }
                }
            }
        }

        /* ---------- FAB ---------- */
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = Color(0xFF6C5CE7),
            onClick = {
                navController.navigate(Routes.CREATE_CLASSROOM)
            }
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White)
        }
    }
}
@Composable
fun ModernClassroomCard(
    title: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E7FF)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.width(18.dp))

            Column {

                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp // slightly bigger text
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Tap to open discussion",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateClassroomSCR(navController: NavController) {

    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    // --- States ---
    var classroomName by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var showFriendSheet by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var permissionGranted by remember { mutableStateOf(false) }
    var selectedFriends by remember { mutableStateOf(setOf<String>()) }

    /* ---------------- PERMISSION LOGIC ---------------- */
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> permissionGranted = granted }

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

    /* ---------------- MAIN UI ---------------- */
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB))
    ) {
        LazyColumn(contentPadding = PaddingValues(bottom = 40.dp)) {

            item {
                GradientHeader(
                    title = "Create Classroom",
                    subtitle = "Build your own learning space 🚀",
                    navController = navController
                )
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Classroom Name
                    OutlinedTextField(
                        value = classroomName,
                        onValueChange = { classroomName = it },
                        label = { Text("Classroom Name") },
                        placeholder = { Text("e.g. Android Masters") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    // Subject Name (Manual Input)
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject Name") },
                        placeholder = { Text("e.g. Kotlin, Science, History") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    /* -------- ADD MEMBERS BUTTON -------- */
                    OutlinedButton(
                        onClick = { showFriendSheet = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Person, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Members from Contacts")
                    }

                    /* -------- SELECTED MEMBERS CHIPS -------- */
                    if (selectedFriends.isNotEmpty()) {
                        Text("Selected Members", fontWeight = FontWeight.SemiBold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedFriends.forEach { number ->
                                AssistChip(
                                    onClick = { selectedFriends = selectedFriends - number },
                                    label = { Text(number) }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    /* -------- CREATE CLASSROOM BUTTON -------- */
                    Button(
                        onClick = {
                            if (classroomName.isBlank() || subject.isBlank() || currentUser == null) return@Button

                            isLoading = true
                            val classroomId = firestore.collection("classrooms").document().id
                            val inviteCode = classroomId.take(6).uppercase()

                            val members = mutableListOf<String>()
                            members.add(currentUser.uid)

                            val classroomData = hashMapOf(
                                "name" to classroomName.trim(),
                                "subject" to subject.trim(),
                                "inviteCode" to inviteCode,
                                "createdBy" to currentUser.uid,
                                "members" to members,
                                "createdAt" to FieldValue.serverTimestamp()
                            )

                            firestore.collection("classrooms").document(classroomId)
                                .set(classroomData)
                                .addOnSuccessListener {
                                    isLoading = false
                                    navController.navigate("classroom_chat/$classroomId") {
                                        popUpTo(Routes.CREATE_CLASSROOM) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Create Classroom", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }

    /* ---------------- BOTTOM SHEET ---------------- */
    if (showFriendSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFriendSheet = false }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                Text("Select Friends", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                    items(contacts) { contact ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            onClick = {
                                selectedFriends = if (selectedFriends.contains(contact.number))
                                    selectedFriends - contact.number
                                else
                                    selectedFriends + contact.number
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selectedFriends.contains(contact.number),
                                    onCheckedChange = null // Handled by Card onClick
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(contact.name, fontWeight = FontWeight.Medium)
                                    Text(contact.number, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { showFriendSheet = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}
@Composable
fun GradientHeader(
    title: String,
    subtitle: String,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4F46E5),
                        Color(0xFF6D28D9)
                    )
                ),
                shape = RoundedCornerShape(
                    bottomStart = 36.dp,
                    bottomEnd = 36.dp
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {

        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        null,
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                subtitle,
                color = Color.White.copy(0.85f),
                fontSize = 14.sp
            )
        }
    }
}