package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.classroom.CreateClassroomScreen
import com.example.learningai.home.ChatHomeScreen
import com.example.learningai.home.ResultScreen
import com.example.learningai.classroom.QuestionsScreen
import com.example.learningai.user.LoginScreen
import com.example.learningai.user.UserInputSCR
import com.example.learningai.user.UserProfileSCR

@Composable
fun AppNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = Modifier.padding(paddingValues)
    ) {

        composable(Routes.HOME) {
            ChatHomeScreen(navController)
        }

        composable(Routes.CREATE_CLASSROOM) {
            CreateClassroomScreen(navController)
        }

        composable(Routes.CHAT) {
            UserInputSCR(navController)
        }

        composable(Routes.PROFILE) {
            UserProfileSCR(
                navController = navController,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable("${Routes.QUESTIONSCREEN}/{classroomId}") { backStack ->
            QuestionsScreen(
                navController = navController,
                classroomId = backStack.arguments?.getString("classroomId") ?: ""
            )
        }

        composable("${Routes.RESULT}/{classroomId}/{score}/{total}") { backStack ->
            ResultScreen(
                classroomId = backStack.arguments?.getString("classroomId") ?: "",
                score = backStack.arguments?.getString("score")?.toIntOrNull() ?: 0,
                totalQuestions = backStack.arguments?.getString("total")?.toIntOrNull() ?: 0,
                userId = "demo_user",
                navController = navController
            )
        }
    }
}