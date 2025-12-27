package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.Admin.AdminScreen
import com.example.learningai.home.*
import com.example.learningai.user.*
import com.example.learningai.viewmodel.InterviewViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    val interviewViewModel: InterviewViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = Modifier.padding(paddingValues)
    ) {

        composable(Routes.HOME) {
            HomeSCR(navController)
        }

        composable("${Routes.INTERVIEW}/{subjectId}") { backStack ->
            val subjectId = backStack.arguments?.getString("subjectId") ?: ""

            InterviewScreen(
                subjectId = subjectId,
                viewModel = interviewViewModel,
                onFinish = {
                    navController.navigate("${Routes.RESULT}/$subjectId")
                }
            )
        }

        composable("${Routes.RESULT}/{subjectId}") { backStack ->
            val subjectId = backStack.arguments?.getString("subjectId") ?: ""

            ResultScreen(
                subjectId = subjectId,
                viewModel = interviewViewModel,
                onFinish = {
                    interviewViewModel.resetQuiz()
                    navController.popBackStack(Routes.HOME, false)
                }
            )
        }

        composable(Routes.CHAT) { UserInputSCR() }

        composable(Routes.PROFILE) {
            UserProfileSCR(navController)
        }

        composable(Routes.ADMIN) {
            AdminScreen()
        }
    }
}