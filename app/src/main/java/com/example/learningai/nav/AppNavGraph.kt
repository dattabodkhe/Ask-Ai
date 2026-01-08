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
import com.example.learningai.quiz.ResultScreen
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

        composable(
            route = "${Routes.ADD_QUESTIONS}/{subject}/{count}"
        ) { backStack ->

            val subject =
                backStack.arguments?.getString("subject") ?: ""
            val count =
                backStack.arguments?.getString("count")?.toInt() ?: 5

            val vm: AddQuestionsViewModel = viewModel()

            AddQuestionsScreen(
                navController = navController,
                subject = subject,
                totalQuestions = count,
                viewModel = vm
            )
        }

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
                userId = "demo_user", // 🔜 Firebase Auth se aayega
                navController = navController
            )
        }

        composable(Routes.CHAT) {
            UserInputSCR(navController)
        }

        composable(Routes.PROFILE) {
            UserProfileSCR(navController)
        }
    }
}