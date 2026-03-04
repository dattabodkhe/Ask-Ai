package com.example.learningai.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinClassroomScreen(
    navController: NavController,
    initialInviteCode: String? = null
) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var inviteCodeInput by remember { mutableStateOf(initialInviteCode ?: "") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun extractCode(input: String): String {
        return if (input.contains("/join/")) {
            input.substringAfterLast("/join/").trim()
        } else {
            input.trim()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Fixed: Adaptive Background
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("Join Classroom", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("Enter code or paste link to join 🚀",
                        color = Color.White.copy(0.85f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 48.dp))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = inviteCodeInput,
                onValueChange = {
                    inviteCodeInput = it
                    errorMessage = ""
                },
                label = { Text("Invite Code or Link") },
                placeholder = { Text("Example: ABC123XYZ") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            if (errorMessage.isNotBlank()) {
                Text(errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.Start))
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val finalCode = extractCode(inviteCodeInput)
                    if (finalCode.isEmpty()) {
                        errorMessage = "Please enter a valid code"
                        return@Button
                    }

                    isLoading = true
                    firestore.collection("classrooms")
                        .whereEqualTo("inviteCode", finalCode)
                        .get()
                        .addOnSuccessListener { result ->
                            if (!result.isEmpty) {
                                val doc = result.documents.first()
                                joinProcess(doc.id, doc.get("members") as? List<*>, firestore, currentUser?.uid, navController)
                            } else {
                                errorMessage = "Invalid code or link 😕"
                            }
                            isLoading = false
                        }
                        .addOnFailureListener {
                            errorMessage = "Check your connection"
                            isLoading = false
                        }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Join Group", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Keep the private helper functions as they are, but ensures consistent color mapping.
private fun joinProcess(classId: String, members: List<*>?, db: FirebaseFirestore, userId: String?, navController: NavController) {
    if (userId != null) {
        val memberList = members?.toMutableList() ?: mutableListOf<Any>()
        if (!memberList.contains(userId)) {
            db.collection("classrooms").document(classId).update("members", FieldValue.arrayUnion(userId))
        }
        navController.navigate("classroom_chat/$classId")
    }
}