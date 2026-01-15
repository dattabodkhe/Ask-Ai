package com.example.learningai.nav

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navController: NavController,
    showBack: Boolean,
    currentRoute: String?
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (currentRoute == Routes.HOME) {
                TextButton(
                    onClick = {
                        navController.navigate(Routes.CREATE_CLASSROOM)
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Create")
                }
            }
        }
    )
}