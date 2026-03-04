package com.example.learningai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* ================================================= */
/* 1. BRAND & COMMON COLORS */
/* ================================================= */
val Primary = Color(0xFF5B5EF7)
val Secondary = Color(0xFF8B5CF6)
val NeonBlue = Color(0xFF22D3EE)

/* Backward compatibility */
val Purple = Primary

/* ================================================= */
/* 2. LIGHT MODE PALETTE */
/* ================================================= */
val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)
val CardLight = Color(0xFFF1F5F9)
val TextDark = Color(0xFF0F172A)
val TextGray = Color(0xFF64748B)

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = CardLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextGray
)

/* ================================================= */
/* 3. DARK MODE PALETTE */
/* ================================================= */
val DarkPrimary = Color(0xFF8B5CF6) // Dark mode mein purple zyada glow karega
val DarkSecondary = Color(0xFF22D3EE)
val BackgroundDark = Color(0xFF0F172A) // Deep Navy
val SurfaceDark = Color(0xFF1E293B)    // Steel Gray
val CardDark = Color(0xFF1E293B)
val TextWhite = Color(0xFFE5E7EB)
val TextLightGray = Color(0xFF94A3B8)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = CardDark,
    onPrimary = Color.White,
    onSecondary = Color(0xFF0F172A),
    onBackground = TextWhite,
    onSurface = TextWhite,
    onSurfaceVariant = TextLightGray
)

/* ================================================= */
/* 4. THEME CONFIGURATION */
/* ================================================= */
@Composable
fun LearningAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        // typography = Typography, // Ensure Typography.kt is correctly linked
        content = content
    )
}