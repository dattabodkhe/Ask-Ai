package com.example.learningai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = NeonBlue,
    background = DarkBlue,
    surface = CardDark,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun LearningAiTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )

}
val appGradient = Brush.verticalGradient(
    colors = listOf(
        DarkBlue,
        CardDark
    )
)