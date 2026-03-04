package com.example.learningai.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LearningAISplash(onAnimationFinished: () -> Unit) {

    // Logo Bounce Animation
    val infiniteTransition = rememberInfiniteTransition(label = "logoBounce")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scaleAnimation"
    )

    LaunchedEffect(Unit) {
        delay(3000) // 3 Seconds wait
        onAnimationFinished()
    }

    // Indigo to Dark Navy Gradient
    val splashGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4F46E5),
            Color(0xFF7C3AED),
            Color(0xFF1E1B4B)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {

            // --- GEOMETRIC CAT LOGO ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val catColor = Color.White

                    // Left Ear
                    drawPath(Path().apply {
                        moveTo(width * 0.15f, height * 0.3f)
                        lineTo(width * 0.35f, height * 0.5f)
                        lineTo(width * 0.1f, height * 0.55f)
                        close()
                    }, color = catColor, style = Fill)

                    // Right Ear
                    drawPath(Path().apply {
                        moveTo(width * 0.85f, height * 0.3f)
                        lineTo(width * 0.65f, height * 0.5f)
                        lineTo(width * 0.9f, height * 0.55f)
                        close()
                    }, color = catColor, style = Fill)

                    // Face Structure
                    drawPath(Path().apply {
                        moveTo(width * 0.25f, height * 0.5f)
                        lineTo(width * 0.75f, height * 0.5f)
                        lineTo(width * 0.85f, height * 0.75f)
                        lineTo(width * 0.5f, height * 0.95f)
                        lineTo(width * 0.15f, height * 0.75f)
                        close()
                    }, color = catColor, style = Fill)

                    // Smart Eyes
                    drawRect(
                        color = Color(0xFF1E1B4B),
                        topLeft = Offset(width * 0.33f, height * 0.62f),
                        size = Size(width * 0.12f, height * 0.05f)
                    )
                    drawRect(
                        color = Color(0xFF1E1B4B),
                        topLeft = Offset(width * 0.55f, height * 0.62f),
                        size = Size(width * 0.12f, height * 0.05f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // 1. CENTER MAIN NAME
            Text(
                text = "Learning-AI",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )

            // 2. TAGLINE
            Text(
                text = "Learn with AI",
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 4.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 3. APP REPRESENTATION LINE
            Text(
                text = "The Future of Intelligent Learning is Here.",
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                lineHeight = 22.sp
            )
        }

        // 4. BOTTOM CREDITS (DRB TEAM)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "developed by",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "[ ",
                    color = Color(0xFF7C3AED),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "DRB TEAM",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = " ]",
                    color = Color(0xFF7C3AED),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}