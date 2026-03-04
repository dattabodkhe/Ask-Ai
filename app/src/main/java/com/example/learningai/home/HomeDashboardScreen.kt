package com.example.learningai.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.learningai.classroom.ModernClassroomCard
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(navController: NavHostController) {

    val firestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var classroomList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var fabExpanded by remember { mutableStateOf(false) }

    // --- Stats States ---
    var questionsSolved by remember { mutableIntStateOf(0) }
    var groupsJoined by remember { mutableIntStateOf(0) }

    /* -------- LOAD DATA -------- */
    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                // 1. Load Classrooms
                val result = firestore.collection("classrooms")
                    .whereArrayContains("members", uid)
                    .get().await()
                classroomList = result.documents.map {
                    Pair(it.id, it.getString("name") ?: "Untitled")
                }

                // 2. Load Solved Questions Count (Collection Group)
                val questionSnapshot = firestore.collectionGroup("messages")
                    .whereEqualTo("senderId", uid)
                    .whereEqualTo("type", "ai_questions")
                    .get().await()
                questionsSolved = questionSnapshot.size()

                // 3. Load Groups Count
                groupsJoined = classroomList.size

            } catch (e: Exception) {
                Log.e("HOME", "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Rank Logic
    val rank = when {
        questionsSolved >= 1000 -> "DGP"
        questionsSolved >= 500 -> "Comm."
        questionsSolved >= 200 -> "S.P."
        questionsSolved >= 100 -> "Insp."
        questionsSolved >= 50 -> "S.I."
        questionsSolved >= 10 -> "Const."
        else -> "Tr."
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            /* -------- HEADER -------- */
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFF4F46E5), Color(0xFF6D28D9))),
                            shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        /* Profile Row */
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val displayName = currentUser?.displayName ?: "User"
                            val firstLetter = displayName.trim().firstOrNull()?.uppercase() ?: "U"

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable { navController.navigate(Routes.USER_PROFILE) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(firstLetter, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF4F46E5))
                            }

                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Welcome back 👋", color = Color.White.copy(0.85f), fontSize = 13.sp)
                                Text("Hi $displayName", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(Modifier.height(30.dp))

                        /* Updated Stats Row (Solved, Groups, Rank) */
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(questionsSolved.toString(), "Solved", Modifier.weight(1f))
                            StatCard(groupsJoined.toString(), "Groups", Modifier.weight(1f))
                            StatCard(rank, "Rank", Modifier.weight(1f))
                        }
                    }
                }
            }

            item { SectionTitle("Quick Actions") }
            item { JoinClassroomCard { navController.navigate(Routes.JOIN_CLASSROOM) } }

            item { SectionTitle("Your Classrooms") }

            item {
                if (isLoading) {
                    Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF6D28D9))
                    }
                } else if (classroomList.isEmpty()) {
                    EmptyClassroomCard { navController.navigate(Routes.CREATE_CLASSROOM) }
                } else {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        classroomList.forEach { classroom ->
                            ModernClassroomCard(title = classroom.second) {
                                navController.navigate("classroom_chat/${classroom.first}")
                            }
                        }
                    }
                }
            }
        }

        /* -------- EXPANDABLE FAB -------- */
        Column(
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            horizontalAlignment = Alignment.End
        ) {
            if (fabExpanded) {
                ExtendedFloatingActionButton(
                    text = { Text("Add Friend") },
                    icon = { Icon(Icons.Default.Person, null) },
                    onClick = { fabExpanded = false; navController.navigate(Routes.CONTACTS) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                ExtendedFloatingActionButton(
                    text = { Text("Create Question") },
                    icon = { Icon(Icons.Default.Add, null) },
                    onClick = { fabExpanded = false; navController.navigate(Routes.CREATE_AI_QUESTION) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            FloatingActionButton(
                containerColor = Color(0xFF6C5CE7),
                onClick = { fabExpanded = !fabExpanded }
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    }
}

/* --- Helper Composables --- */

@Composable
fun SectionTitle(text: String) {
    Text(text, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier) {
    Card(
        modifier = modifier.height(85.dp), // Fixed height for better look
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.15f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(label, color = Color.White.copy(0.8f), fontSize = 11.sp)
        }
    }
}

@Composable
fun JoinClassroomCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, null, tint = Color(0xFF4F46E5))
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Join Classroom", fontWeight = FontWeight.SemiBold)
                Text("Collaborate with peers", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun EmptyClassroomCard(onCreateClick: () -> Unit) {
    Card(modifier = Modifier.padding(16.dp).fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No Classrooms Yet 😢", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onCreateClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D28D9))) {
                Text("Create Classroom")
            }
        }
    }
}
