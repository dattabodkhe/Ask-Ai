package com.example.learningai.nav

import android.media.tv.StreamEventRequest
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(val title : String,
    val route : String,
    val icon : ImageVector) {
}