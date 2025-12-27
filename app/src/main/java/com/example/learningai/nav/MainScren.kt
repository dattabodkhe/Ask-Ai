package com.example.learningai.nav

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.learningai.ViewModel.AuthViewModel

import com.example.learningai.ui.nav.BottomAppBar
import com.example.learningai.user.LoginScreen

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (!isLoggedIn) {

        LoginScreen(
            authViewModel = authViewModel,
            onSuccess = {
                // nothing here, state auto-update karega
            }
        )

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
        ) { paddingValues ->
            AppNavGraph(
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}