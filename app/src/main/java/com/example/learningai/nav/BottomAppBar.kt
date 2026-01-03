package com.example.learningai.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.learningai.nav.BottomNavItem
import com.example.learningai.nav.Routes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

@Composable
fun BottomAppBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    Surface(
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
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
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == Routes.PROFILE,
                onClick = { onItemClick(Routes.PROFILE) }
            )
        }
    }
}