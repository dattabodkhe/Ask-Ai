package com.example.learningai.nav

fun getTitleForRoute(route: String?): String {
    return when {
        route == Routes.HOME -> "Home"
        route == Routes.CHAT -> "AI Chat"
        route == Routes.PROFILE -> "Profile"
        route?.startsWith(Routes.RESULT) == true -> "Result"
        else -> "Learning AI"
    }
}