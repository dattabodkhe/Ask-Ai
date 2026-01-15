package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.nav.Routes
import com.example.learningai.ui.theme.*



@Composable
fun UserProfileSCR( onLogout: () -> Boolean,
                   navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        // 🔵 Avatar
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Purple, NeonBlue)),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Datta Bodkhe",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "datta@example.com",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 🔹 Profile Options
        ProfileItem(
            icon = Icons.Default.Settings,
            title = "App Version",
            subtitle = "1.0.0"
        )

        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Privacy Policy",
            subtitle = "Read our policies"
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Logout
        Button(
            onClick = {
                navController.navigate(Routes.LOGIN){
                    popUpTo(0){ inclusive = true}
                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.85f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Delete, contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = NeonBlue)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Medium)
                Text(subtitle, color = Color.LightGray, fontSize = 12.sp)
            }
        }
    }
}