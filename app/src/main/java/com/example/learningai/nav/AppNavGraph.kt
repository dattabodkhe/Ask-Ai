package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.MVVM.AddQuestionsViewModel
import com.example.learningai.classroom.AddQuestionsScreen
import com.example.learningai.classroom.CreateClassroomScreen
import com.example.learningai.home.ChatHomeScreen
import com.example.learningai.home.ResultScreen
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

        /* ---------------- LOGIN ---------------- */
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        /* ---------------- HOME ---------------- */
        composable(Routes.HOME) {
            ChatHomeScreen(navController)
        }

        /* ---------------- CREATE CLASSROOM ---------------- */
        composable(Routes.CREATE_CLASSROOM) {
            CreateClassroomScreen(navController)
        }

        /* ---------------- ADD QUESTIONS (IMPORTANT) ---------------- */
        composable(
            route = "${Routes.QUESTIONSCREEN}/{classroomId}/{subject}/{count}"
        ) { backStack ->

            val classroomId =
                backStack.arguments?.getString("classroomId") ?: ""
            val subject =
                backStack.arguments?.getString("subject") ?: ""
            val count =
                backStack.arguments?.getString("count")?.toInt() ?: 5

            val viewModel: AddQuestionsViewModel = viewModel()

            AddQuestionsScreen(
                navController = navController,
                classroomId = classroomId,
                subject = subject,
                totalQuestions = count,
                viewModel = viewModel
            )
        }

        /* ---------------- RESULT ---------------- */
        composable(
            route = "${Routes.RESULT}/{classroomId}/{score}/{total}"
        ) { backStack ->

            val classroomId =
                backStack.arguments?.getString("classroomId") ?: ""
            val score =
                backStack.arguments?.getString("score")?.toInt() ?: 0
            val total =
                backStack.arguments?.getString("total")?.toInt() ?: 0

            ResultScreen(
                classroomId = classroomId,
                score = score,
                totalQuestions = total,
                userId = "demo_user",
                navController = navController
            )
        }

        /* ---------------- AI CHAT ---------------- */
        composable(Routes.CHAT) {
            UserInputSCR(navController)
        }

        /* ---------------- PROFILE ---------------- */
        composable(Routes.PROFILE) {
            UserProfileSCR(
                navController = navController,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0): { inclusive = true }
                    }
                }
            )
        }
    }
}