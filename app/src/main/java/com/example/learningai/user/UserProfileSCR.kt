package com.example.learningai.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileSCR(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    // --- States ---
    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    var questionsSolved by remember { mutableIntStateOf(0) }
    var groupsJoined by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // --- Data Fetching ---
    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            try {
                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                userData = userDoc.data

                val questionSnapshot = firestore.collectionGroup("messages")
                    .whereEqualTo("senderId", currentUser.uid)
                    .whereEqualTo("type", "ai_questions")
                    .get().await()
                questionsSolved = questionSnapshot.size()

                val groupSnapshot = firestore.collection("classrooms")
                    .whereArrayContains("members", currentUser.uid)
                    .get().await()
                groupsJoined = groupSnapshot.size()

            } catch (e: Exception) {
                Log.e("PROFILE", "Error fetching profile: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Bhai, logout karna hai?") },
            confirmButton = {
                Button(onClick = {
                    showLogoutDialog = false
                    auth.signOut()
                    navController.navigate(Routes.ROLE_SELECTION) { popUpTo(0) { inclusive = true } }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Logout", color = Color.White)
                }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }

    // --- Police Rank Logic ---
    val (rank, badgeDesc) = when {
        questionsSolved >= 1000 -> "DGP" to "Learning ke supreme head! 👮‍♂️💎"
        questionsSolved >= 500 -> "Commissioner" to "City aapke control mein hai! 🏛️"
        questionsSolved >= 200 -> "S.P." to "Expert problem solver! 🚔"
        questionsSolved >= 100 -> "Inspector" to "Aapka dimag tez chalta hai! 🔍"
        questionsSolved >= 50 -> "Sub-Inspector" to "Consistency ka power! ⭐⭐"
        questionsSolved >= 10 -> "Constable" to "Duty par tainaat! 🛡️"
        else -> "Trainee" to "Pehle 10 sawal solve karke Constable bano! 🏫"
    }

    val purpleGradient = Brush.verticalGradient(listOf(Color(0xFF9C27B0), Color(0xFF673AB7)))

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF673AB7))
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6)).verticalScroll(rememberScrollState())
        ) {
            /* --- Header Section (Fixed Padding) --- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(purpleGradient, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                // Top Bar (Back Arrow, Title & Menu)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                    Text(
                        "My Profile",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )

                    // --- MENU OPTIONS ---
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, null, tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            // 1. SETTINGS (Naya Option)
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Settings, null, modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Settings")
                                    }
                                },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Routes.SETTINGS) // Shortcut settings screen par jane ke liye
                                }
                            )

                            // 2. PRIVACY POLICY
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lock, null, modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Privacy Policy")
                                    }
                                },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Routes.PRIVACY_POLICY)
                                }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                            // 3. LOGOUT
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Logout", color = Color.Red)
                                    }
                                },
                                onClick = { menuExpanded = false; showLogoutDialog = true }
                            )
                        }
                    }
                }

                // Center Content (Avatar & Name)
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 45.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val name = userData?.get("name")?.toString() ?: "User"
                    // Profile Circle with Dynamic First Letter
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(85.dp),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = name.trim().firstOrNull()?.uppercase() ?: "U",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF673AB7)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(userData?.get("role")?.toString() ?: "Student", color = Color.White.copy(0.7f), fontSize = 14.sp)
                }
            }

            /* --- Stats Section --- */
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = (-30).dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatCard("Solved", questionsSolved.toString(), Color(0xFF9C27B0))
                ProfileStatCard("Groups", groupsJoined.toString(), Color(0xFF673AB7))
                ProfileStatCard("Rank", rank, Color(0xFFF97316))
            }

            /* --- Institution Details --- */
            WhiteCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Institution Details", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1F2937))
                Spacer(Modifier.height(16.dp))
                InfoRow(Icons.Default.Home, "College", userData?.get("collegeName")?.toString() ?: "Not Set")
                InfoRow(Icons.Default.LocationOn, "Location", "${userData?.get("state") ?: "N/A"}, ${userData?.get("country") ?: "N/A"}")
                InfoRow(Icons.Default.Email, "Email", currentUser?.email ?: "No Email")
            }

            Spacer(Modifier.height(16.dp))

            /* --- Achievements Section --- */
            WhiteCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("Department Status", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1F2937))
                Spacer(Modifier.height(12.dp))
                AchievementRow(rank, badgeDesc, questionsSolved)
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
/* --- Helpers --- */

@Composable
fun ProfileStatCard(label: String, value: String, textColor: Color) {
    Card(
        modifier = Modifier.size(width = 100.dp, height = 90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = textColor)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun WhiteCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun InfoRow(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = Color(0xFFF3E5F5), modifier = Modifier.size(36.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1F2937))
        }
    }
}

@Composable
fun AchievementRow(title: String, subtitle: String, count: Int) {
    val iconColor = when {
        count >= 750 -> Color(0xFFFFD700) // Gold
        count >= 250 -> Color(0xFFC0C0C0) // Silver
        else -> Color(0xFFCD7F32)        // Bronze
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AccountBox, null, tint = iconColor, modifier = Modifier.size(45.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF1F2937))
            Text(subtitle, fontSize = 14.sp, color = Color.Gray, lineHeight = 18.sp)
        }
    }
}