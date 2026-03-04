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
    var questionsSolved by remember { mutableIntStateOf(0) }
    var groupsJoined by remember { mutableIntStateOf(0) }

    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val result = firestore.collection("classrooms")
                    .whereArrayContains("members", uid)
                    .get().await()
                classroomList = result.documents.map {
                    Pair(it.id, it.getString("name") ?: "Untitled")
                }

                val questionSnapshot = firestore.collectionGroup("messages")
                    .whereEqualTo("senderId", uid)
                    .whereEqualTo("type", "ai_questions")
                    .get().await()
                questionsSolved = questionSnapshot.size()
                groupsJoined = classroomList.size
            } catch (e: Exception) {
                Log.e("HOME", "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    val rank = when {
        questionsSolved >= 1000 -> "DGP"
        questionsSolved >= 500 -> "Comm."
        questionsSolved >= 200 -> "S.P."
        questionsSolved >= 100 -> "Insp."
        questionsSolved >= 50 -> "S.I."
        questionsSolved >= 10 -> "Const."
        else -> "Tr."
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            /* -------- HEADER -------- */
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                            ),
                            shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val displayName = currentUser?.displayName ?: "User"
                            val firstLetter = displayName.trim().firstOrNull()?.uppercase() ?: "U"

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)) // Glassmorphism touch
                                    .clickable { navController.navigate(Routes.USER_PROFILE) },
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(shape = CircleShape, color = Color.White) {
                                    Text(
                                        text = firstLetter,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Welcome back 👋", color = Color.White.copy(0.8f), fontSize = 13.sp)
                                Text("Hi $displayName", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(Modifier.height(30.dp))

                        /* Stats Row */
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    text = { Text("Add Friend") },
                    icon = { Icon(Icons.Default.Person, null) },
                    onClick = { fabExpanded = false; navController.navigate(Routes.CONTACTS) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                ExtendedFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    text = { Text("Create Question") },
                    icon = { Icon(Icons.Default.Add, null) },
                    onClick = { fabExpanded = false; navController.navigate(Routes.CREATE_AI_QUESTION) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { fabExpanded = !fabExpanded }
            ) {
                Icon(if (fabExpanded) Icons.Default.Add else Icons.Default.Add, null)
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier.height(90.dp),
        color = Color.White.copy(alpha = 0.15f), // Glassmorphism effect
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            Text(label, color = Color.White.copy(0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
fun JoinClassroomCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primary.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Join Classroom", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("Collaborate with peers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun EmptyClassroomCard(onCreateClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No Classrooms Yet 😢", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Create Classroom")
            }
        }
    }
}