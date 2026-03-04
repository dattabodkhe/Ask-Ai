package com.example.learningai.ui.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learningai.nav.BottomNavItem
import com.example.learningai.nav.Routes

@Composable
fun BottomAppBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .height(72.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == Routes.HOME,
                onClick = { onItemClick(Routes.HOME) }
            )

            BottomNavItem(
                icon = Icons.Default.Email,
                label = "Chat",
                selected = currentRoute == Routes.CHAT,
                onClick = { onItemClick(Routes.CHAT) }
            )

            BottomNavItem(
                icon = Icons.Default.Face,
                label = "Classroom",
                selected = currentRoute == Routes.CLASSROOM,
                onClick = { onItemClick(Routes.CLASSROOM) }
            )

            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == Routes.USER_PROFILE,
                onClick = { onItemClick(Routes.USER_PROFILE) }
            )
        }
    }
}