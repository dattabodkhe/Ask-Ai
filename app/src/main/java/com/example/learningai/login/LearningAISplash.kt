package com.example.learningai.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import com.example.learningai.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
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
        targetValue = 1.05f, // Thoda subtle bounce
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scaleAnimation"
    )

    LaunchedEffect(Unit) {
        delay(3000)
        onAnimationFinished()
    }

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
// --- IMAGE BASED LOGO (100% Match) ---
            // --- IMAGE BASED LOGO (With Internal Circular Clip) ---
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .background(Color.White, shape = androidx.compose.foundation.shape.CircleShape)
                    .padding(20.dp), // Circle ke andar thodi jagah chhodne ke liye
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.applogo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        // Yahan fix hai: Is image content ko bhi circular cut (clip) karein
                        .clip(androidx.compose.foundation.shape.CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop // Fit se Crop kiya taaki wo white circle ko fill kare
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Text Section (Matching your design)
            Text(
                text = "Learning-AI",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )

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

        // Bottom Credits
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
                Text(text = "[ ", color = Color(0xFF7C3AED), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "DRB TEAM", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text(text = " ]", color = Color(0xFF7C3AED), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}