package com.example.learningai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.nav.MainScreen
import com.example.learningai.ui.theme.LearningAiTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel =
            ViewModelProvider(this)[AuthViewModel::class.java]

        setContent {
            LearningAiTheme {
                MainScreen(authViewModel = authViewModel)
            }
        }
    }
}
