package com.example.learningai

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.nav.MainScreen
import com.example.learningai.ui.splash.LearningAISplash
import com.example.learningai.ui.theme.LearningAiTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install Splash API
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { false }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LearningAiTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    // Yahan aapki asli "LearningAISplash" (Cat Logo wali) dikhegi
                    LearningAISplash(onAnimationFinished = {
                        showSplash = false
                    })
                } else {
                    // Main Content
                    MainScreen(authViewModel = ViewModelProvider(this)[AuthViewModel::class.java])
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