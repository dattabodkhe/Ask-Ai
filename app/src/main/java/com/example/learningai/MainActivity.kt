package com.example.learningai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.learningai.ui.theme.LearningAiTheme
import com.example.learningai.user.UserInputSCR
import androidx.core.view.WindowCompat
import com.example.learningai.home.HomeSCR

import com.example.learningai.nav.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningAiTheme {
                MainScreen()
            }
        }
    }
}