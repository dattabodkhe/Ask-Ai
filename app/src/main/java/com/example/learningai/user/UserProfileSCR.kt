package com.example.learningai.user

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileSCR(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // --- DATA STATES ---
    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    var questionsSolved by remember { mutableIntStateOf(0) }
    var groupsJoined by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // --- UI INTERACTION STATES ---
    var showMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }

    // --- FORM STATES (FOR EDITING) ---
    var collegeName by remember { mutableStateOf("") }
    var universityName by remember { mutableStateOf("") }
    var stateName by remember { mutableStateOf("") }

    // Email se Name nikalne ka logic
    val finalName = remember(userData, currentUser) {
        val nameFromDb = userData?.get("name")?.toString()
        if (!nameFromDb.isNullOrBlank()) nameFromDb
        else currentUser?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() } ?: "User"
    }

    // --- REAL-TIME LISTENERS ---
    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            // 1. User Data & Questions Listener
            firestore.collection("users").document(currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (snapshot != null && snapshot.exists()) {
                        userData = snapshot.data
                        questionsSolved = (snapshot.getLong("questionsSolved") ?: 0L).toInt()

                        // Form values sync karna agar user edit nahi kar raha
                        if (!showEditSheet) {
                            collegeName = snapshot.getString("collegeName") ?: ""
                            universityName = snapshot.getString("universityName") ?: ""
                            stateName = snapshot.getString("state") ?: ""
                        }
                    }
                    isLoading = false
                }

            // 2. Groups Joined Listener
            firestore.collection("classrooms")
                .whereArrayContains("members", currentUser.uid)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        groupsJoined = snapshot.size()
                    }
                }
        }
    }

    // Rank Logic
    val rankInfo = when {
        questionsSolved >= 100 -> Pair("Pro Player", "Elite league member! 🏆")
        questionsSolved >= 10 -> Pair("Player", "Kafi sahi khel rahe ho! 🎮")
        else -> Pair("Newbie", "Abhi toh game shuru hua hai! 🚀")
    }
    val (rank, badgeDesc) = rankInfo

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
            ) {

                /* --- HEADER SECTION --- */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                            ),
                            shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Text(
                            "My Profile",
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.Settings, null, tint = Color.White)
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.surface).width(180.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    leadingIcon = { Icon(Icons.Default.Settings, null, modifier = Modifier.size(20.dp)) },
                                    onClick = { showMenu = false; navController.navigate(Routes.SETTINGS) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Privacy Policy") },
                                    leadingIcon = { Icon(Icons.Default.Lock, null, modifier = Modifier.size(20.dp)) },
                                    onClick = { showMenu = false; navController.navigate(Routes.PRIVACY_POLICY) }
                                )
                                HorizontalDivider(Modifier.padding(vertical = 4.dp))
                                DropdownMenuItem(
                                    text = { Text("Logout", color = Color.Red) },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.Red) },
                                    onClick = { showMenu = false; showLogoutDialog = true }
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier.size(100.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    finalName.take(1).uppercase(),
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(finalName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Surface(color = Color.White.copy(0.2f), shape = RoundedCornerShape(50)) {
                            Text(
                                userData?.get("role")?.toString() ?: "SELF",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                /* --- STATS SECTION --- */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-40).dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileStatCard("Solved", questionsSolved.toString(), MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    ProfileStatCard("Groups", groupsJoined.toString(), MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                }

                /* --- DETAILS CARD --- */
                Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-20).dp)) {
                    WhiteCard {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Institution Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            TextButton(onClick = { showEditSheet = true }) {
                                Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Edit Info", fontWeight = FontWeight.Bold)
                            }
                        }
                        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                        InfoRow(Icons.Default.Home, "Institution", userData?.get("collegeName")?.toString() ?: "Not Available")
                        InfoRow(Icons.Default.LocationOn, "Location", userData?.get("state")?.toString() ?: "Not Set")
                        InfoRow(Icons.Default.AccountCircle, "University", userData?.get("universityName")?.toString() ?: "N/A")
                    }

                    WhiteCard {
                        Text("Achievement Badge", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        AchievementRow(rank, badgeDesc, questionsSolved)
                    }

                    /* --- LOGOUT CARD --- */
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable { showLogoutDialog = true },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                    ) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.error.copy(0.1f), modifier = Modifier.size(45.dp)) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.padding(10.dp))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Logout Session", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                Text("Aapko firse login karna padega", fontSize = 12.sp, color = MaterialTheme.colorScheme.error.copy(0.7f))
                            }
                            Icon(Icons.Default.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }

    /* --- EDIT SHEET --- */
    if (showEditSheet) {
        ModalBottomSheet(onDismissRequest = { showEditSheet = false }) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth().navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Update Information ✏️", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                OutlinedTextField(value = collegeName, onValueChange = { collegeName = it }, label = { Text("College Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = universityName, onValueChange = { universityName = it }, label = { Text("University Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = stateName, onValueChange = { stateName = it }, label = { Text("State / Location") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

                Button(
                    onClick = {
                        isUpdating = true
                        val updates = mapOf(
                            "collegeName" to collegeName,
                            "universityName" to universityName,
                            "state" to stateName
                        )
                        currentUser?.uid?.let { uid ->
                            firestore.collection("users").document(uid).update(updates)
                                .addOnSuccessListener {
                                    isUpdating = false
                                    showEditSheet = false
                                    Toast.makeText(context, "Details Updated!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    isUpdating = false
                                    Toast.makeText(context, "Update Failed!", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isUpdating
                ) {
                    if (isUpdating) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Save Changes", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    /* --- LOGOUT DIALOG --- */
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Kya aap sach me logout karna chahte hain?") },
            confirmButton = {
                Button(onClick = {
                    auth.signOut()
                    navController.navigate(Routes.ROLE_SELECTION) { popUpTo(0) }
                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Logout")
                }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }
}

/* --- SUPPORTING COMPONENTS --- */

@Composable
fun ProfileStatCard(label: String, value: String, textColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = textColor)
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun WhiteCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun InfoRow(icon: ImageVector, title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun AchievementRow(title: String, subtitle: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(if (count > 50) 0.3f else 0.1f)
        ) {
            Icon(Icons.Default.Star, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(12.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(subtitle, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
