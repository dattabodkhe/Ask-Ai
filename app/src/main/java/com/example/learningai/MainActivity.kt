package com.example.learningai

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.nav.MainScreen
import com.example.learningai.ui.splash.LearningAISplash
import com.example.learningai.ui.theme.LearningAiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Edge-to-edge display ke liye
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LearningAiTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    // Splash screen dikhao aur timer khatam hone par showSplash false karo
                    LearningAISplash(onAnimationFinished = {
                        showSplash = false
                    })
                } else {
                    // Splash ke baad notification permission maango
                    RequestNotificationPermission()
                    // Main app content
                    MainScreen(authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Permission status handled
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}