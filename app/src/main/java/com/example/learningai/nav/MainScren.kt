package com.example.learningai.nav

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
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

    // 🔥 CURRENT ROUTE TRACK
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (!isLoggedIn) {

        // 🔐 LOGIN
        LoginScreen(authViewModel)

    } else {

        Scaffold(
            topBar = {
                AppTopBar(
                    title = getTitleForRoute(currentRoute),
                    navController = navController,
                    showBack = currentRoute != Routes.HOME
                )
            },
            bottomBar = {
                BottomAppBar(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
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
