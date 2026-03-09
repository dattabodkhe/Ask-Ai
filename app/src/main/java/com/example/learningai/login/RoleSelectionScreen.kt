package com.example.learningai.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter // Zaroori Import
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningai.R
import com.example.learningai.model.UserRole

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to LearningAI",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Select your role to personalize your journey.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(40.dp))

            // --- ROLE CARDS USING DRAWABLE ICONS ---
            RoleCard(
                title = "Student",
                description = "Learn, grow, and explore with AI assistance.",
                icon = painterResource(id = R.drawable.teachers), // Drawable icon
                color = MaterialTheme.colorScheme.primary,
                isSelected = selectedRole == UserRole.STUDENT
            ) { selectedRole = UserRole.STUDENT }

            Spacer(Modifier.height(16.dp))

            RoleCard(
                title = "Teacher / Mentor",
                description = "Create groups, teach and inspire students.",
                icon = painterResource(id = R.drawable.school), // Drawable icon
                color = Color(0xFFE91E63),
                isSelected = selectedRole == UserRole.TEACHER
            ) { selectedRole = UserRole.TEACHER }

            Spacer(Modifier.height(16.dp))

            RoleCard(
                title = "Self Learner",
                description = "Explore AI learning tools at your own pace.",
                icon = painterResource(id = R.drawable.student), // Drawable icon
                color = Color(0xFFFF9800),
                isSelected = selectedRole == UserRole.SELF
            ) { selectedRole = UserRole.SELF }

            Spacer(Modifier.height(40.dp))

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
                    Text("Continue", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                }
            }
        }
    }
}

/* -------- UPDATED ROLE CARD COMPONENT -------- */
@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: Painter, // ImageVector ki jagah Painter use kiya
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) color.copy(alpha = 0.1f)
    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    val borderColor = if (isSelected) color else Color.Transparent

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = if (isSelected) color else MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = icon, // Painter based icon render
                        contentDescription = null,
                        tint = if (isSelected) Color.White else color,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
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