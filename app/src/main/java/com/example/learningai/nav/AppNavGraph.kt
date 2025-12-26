package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.home.HomeSCR
import com.example.learningai.home.InterviewScreen
import com.example.learningai.home.NotesSCR
import com.example.learningai.home.ResultScreen
import com.example.learningai.user.UserInputSCR
import com.example.learningai.user.UserProfileSCR
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

        // HOME
        composable(Routes.HOME) {
            HomeSCR(navController)
        }

        // INTERVIEW
        composable("${Routes.INTERVIEW}/{subjectId}") { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""

            InterviewScreen(
                subjectId = subjectId,
                viewModel = interviewViewModel,
                onFinish = { navController.navigate(Routes.RESULT) }
            )
        }

        //  NOTES
        composable("${Routes.NOTES}/{subject}") { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject") ?: ""
            NotesSCR(subject = subject)
        }

        // RESULT
        composable(Routes.RESULT) {
            ResultScreen(
                viewModel = interviewViewModel,
                onRetry = {
                    interviewViewModel.resetQuiz()
                    navController.popBackStack(Routes.HOME, false)
                }
            )
        }

        // CHAT
        composable(Routes.CHAT) {
            UserInputSCR()
        }

        // PROFILE
        composable(Routes.PROFILE) {
            UserProfileSCR()
        }
    }
}