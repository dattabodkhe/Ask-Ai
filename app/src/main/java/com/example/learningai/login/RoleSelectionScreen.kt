package com.example.learningai.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningai.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(
    onNextClick: (UserRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    val bgGradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        )
    )

    Box(
        modifier = Modifier.fillMaxSize().background(bgGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* -------- Title -------- */
            Text(
                text = "Welcome to LearningAI",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Select your role to personalize your journey.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(48.dp))

            /* -------- Role Cards (Standard 2D Icons used) -------- */
            RoleCard(
                title = "Student",
                description = "Learn, grow, and explore with AI assistance.",
                icon = Icons.Default.Home, // 2D Vector Icon
                color = MaterialTheme.colorScheme.primary,
                isSelected = selectedRole == UserRole.STUDENT
            ) { selectedRole = UserRole.STUDENT }

            Spacer(Modifier.height(20.dp))

            RoleCard(
                title = "Teacher / Mentor",
                description = "Create groups, teach and inspire students.",
                icon = Icons.Default.Person, // 2D Vector Icon
                color = Color(0xFFE91E63), // Pink for variety
                isSelected = selectedRole == UserRole.TEACHER
            ) { selectedRole = UserRole.TEACHER }

            Spacer(Modifier.height(20.dp))

            RoleCard(
                title = "Self Learner",
                description = "Explore AI learning tools at your own pace.",
                icon = Icons.Default.Star, // 2D Vector Icon
                color = Color(0xFFFF9800), // Orange
                isSelected = selectedRole == UserRole.SELF
            ) { selectedRole = UserRole.SELF }

            Spacer(Modifier.weight(1f))

            /* -------- Continue Button -------- */
            Button(
                onClick = { selectedRole?.let { onNextClick(it) } },
                enabled = selectedRole != null,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Continue",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                }
            }
        }
    }
}

/* -------- HELPER COMPONENT (Using Vector Graphics) -------- */
@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) color.copy(alpha = 0.08f)
    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    val borderColor = if (isSelected) color else Color.Transparent

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* --- Vector Icon in Circular Background --- */
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = if (isSelected) color else MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else color,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}