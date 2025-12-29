package com.example.learningai.nav

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.user.LoginScreen
import com.example.learningai.ui.nav.BottomAppBar

@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (!isLoggedIn) {
        LoginScreen(authViewModel)
    } else {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    currentRoute = null,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        ) { padding ->
            AppNavGraph(
                navController = navController,
                paddingValues = padding
            )
        }
    }
}
