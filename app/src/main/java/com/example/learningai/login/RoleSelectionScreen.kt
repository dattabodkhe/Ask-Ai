package com.example.learningai.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learningai.model.UserRole

@Composable
fun RoleSelectionScreen(
    onNextClick: (UserRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF4F6FF),
                        Color(0xFFEAEFFF)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            shape = RoundedCornerShape(24.dp),

            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {

                /* -------- Title -------- */

                Text(
                    text = "Choose Your Role",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Select how you'll use LearningAI",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(32.dp))

                /* -------- Cards -------- */

                RoleCard(
                    title = "Student",
                    description = "Learn and grow with AI",
                    icon = Icons.Default.Home,
                    isSelected = selectedRole == UserRole.STUDENT
                ) {
                    selectedRole = UserRole.STUDENT
                }

                Spacer(Modifier.height(16.dp))

                RoleCard(
                    title = "Teacher",
                    description = "Teach and inspire others",
                    icon = Icons.Default.Person,
                    isSelected = selectedRole == UserRole.TEACHER
                ) {
                    selectedRole = UserRole.TEACHER
                }

                Spacer(Modifier.height(16.dp))

                RoleCard(
                    title = "Self Learner",
                    description = "Explore at your own pace",
                    icon = Icons.Default.Email,
                    isSelected = selectedRole == UserRole.SELF
                ) {
                    selectedRole = UserRole.SELF
                }

                Spacer(Modifier.height(32.dp))

                /* -------- Button -------- */

                Button(
                    onClick = {
                        selectedRole?.let {
                            onNextClick(it)
                        }
                    },

                    enabled = selectedRole != null,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                    shape = RoundedCornerShape(14.dp)
                ) {

                    Text("Continue")
                }
            }
        }
    }
}
