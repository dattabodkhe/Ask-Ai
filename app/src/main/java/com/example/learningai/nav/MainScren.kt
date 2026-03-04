package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.ui.nav.BottomAppBar
import com.example.learningai.ui.splash.LearningAISplash

@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        LearningAISplash(onAnimationFinished = {
            showSplash = false
        })
    } else {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // FIX: DETAILS_FORM hata diya gaya hai kyunki wo Routes file ya NavGraph mein nahi hai
        val hideBars = currentRoute?.startsWith(Routes.LOGIN) == true ||
                currentRoute == Routes.ROLE_SELECTION ||
                currentRoute?.startsWith(Routes.QUESTIONSCREEN) == true

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (!hideBars) {
                    BottomAppBar(
                        currentRoute = currentRoute,
                        onItemClick = { route ->
                            if (route != currentRoute) {
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Routes.HOME) {
                                        saveState = true
                                    }
                                }
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            AppNavGraph(
                navController = navController,
                paddingValues = if (hideBars) PaddingValues(0.dp) else paddingValues
            )
        }
    }
}