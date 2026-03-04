package com.example.learningai.classroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupProfileScreen(
    navController: NavController,
    classId: String
) {

    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var classroomName by remember { mutableStateOf("Group") }
    var members by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var createdBy by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    /* ---------------- LOAD DATA ---------------- */

    LaunchedEffect(classId) {
        try {
            val doc = firestore.collection("classrooms")
                .document(classId)
                .get()
                .await()

            classroomName = doc.getString("name") ?: "Group"
            createdBy = doc.getString("createdBy") ?: ""

            val memberUids = doc.get("members") as? List<String> ?: emptyList()

            val tempList = mutableListOf<Pair<String, String>>()

            for (uid in memberUids) {

                val userDoc = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()

                val name =
                    userDoc.getString("name")
                        ?: userDoc.getString("displayName")
                        ?: "Unknown User"

                tempList.add(uid to name)
            }

            members = tempList
            isAdmin = currentUser?.uid == createdBy

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* ---------------- UI ---------------- */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
    ) {

        TopAppBar(
            title = { Text("Group Info") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        /* -------- GROUP PROFILE -------- */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF4F46E5),
                                Color(0xFF7C3AED)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = classroomName.firstOrNull()?.uppercase() ?: "G",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                classroomName,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "${members.size} Members",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(20.dp))

        HorizontalDivider()

        Spacer(Modifier.height(12.dp))

        /* -------- MEMBER LIST -------- */

        LazyColumn {

            items(members) { (uid, name) ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    Color(0xFFE0E7FF),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                name.firstOrNull()?.uppercase() ?: "U",
                                color = Color(0xFF4F46E5)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(name)

                            if (uid == createdBy) {
                                Text(
                                    "Admin",
                                    color = Color(0xFF6C5CE7),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        /* -------- REMOVE BUTTON -------- */

                        if (isAdmin && uid != currentUser?.uid) {

                            TextButton(
                                onClick = {

                                    firestore.collection("classrooms")
                                        .document(classId)
                                        .update(
                                            "members",
                                            FieldValue.arrayRemove(uid)
                                        )
                                }
                            ) {
                                Text("Remove", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}