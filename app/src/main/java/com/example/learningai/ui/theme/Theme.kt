package com.example.learningai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


/* -------- LIGHT -------- */

private val LightColors = lightColorScheme(

    primary = Primary,
    secondary = Secondary,

    background = BackgroundLight,
    surface = SurfaceLight,

    onPrimary = Color.White,
    onSecondary = Color.White,

    onBackground = TextDark,
    onSurface = TextDark
)


/* -------- DARK -------- */

private val DarkColors = darkColorScheme(

    primary = DarkPrimary,
    secondary = DarkSecondary,

    background = BackgroundDark,
    surface = SurfaceDark,

    onPrimary = TextWhite,
    onSecondary = TextWhite,

    onBackground = TextWhite,
    onSurface = TextWhite
)


@Composable
fun LearningAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // ✅ AUTO
    content: @Composable () -> Unit
) {

    val colors =
        if (darkTheme) DarkColors else LightColors


    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}


/* -------- Gradient -------- */

@Composable
fun appGradient(): Brush {

    val dark = isSystemInDarkTheme()

    return Brush.verticalGradient(
        colors =
            if (dark)
                listOf(BackgroundDark, SurfaceDark)
            else
                listOf(Primary, Secondary)
    )
}
