package com.example.learningai.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (!isLoggedIn) {

        LoginScreen(
            navController = navController,
            authViewModel = authViewModel
        )

    } else {

        Scaffold(
            modifier = Modifier.fillMaxSize(),

            topBar = {
                AppTopBar(
                    title = "Learning AI",
                    navController = navController,
                    showBack = currentRoute != Routes.HOME,
                    currentRoute = currentRoute
                )
            },

            bottomBar = {
                BottomAppBar(
                    currentRoute = currentRoute,
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