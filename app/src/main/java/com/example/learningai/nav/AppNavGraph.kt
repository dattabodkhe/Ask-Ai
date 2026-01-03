package com.example.learningai.nav

import NotesSCR
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.Admin.AddQuestionScreen
import com.example.learningai.Admin.AdminDashboardScreen
import com.example.learningai.home.*
import com.example.learningai.user.*
import com.example.learningai.MVVM.InterviewViewModel

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

        //  HOME
        composable(Routes.HOME) {
            HomeSCR(navController)
        }

        //  NOTES
        composable("${Routes.NOTES}/{subjectId}") { backStack ->
            val subjectId = backStack.arguments?.getString("subjectId") ?: ""
            NotesSCR(subjectId)
        }

        //  QUESTIONS / INTERVIEW
        composable("${Routes.INTERVIEW}/{subjectId}") { backStack ->
            val subjectId = backStack.arguments?.getString("subjectId") ?: ""
            val vm: InterviewViewModel = viewModel()

            InterviewScreen(
                subjectId = subjectId,
                viewModel = vm,
                onFinish = {
                    navController.navigate("${Routes.RESULT}/$subjectId")
                }
            )
        }

        //  RESULT
        composable("${Routes.RESULT}/{subjectId}") { backStack ->
            val subjectId = backStack.arguments?.getString("subjectId") ?: ""
            val vm: InterviewViewModel = viewModel()

            ResultScreen(
                subjectId = subjectId,
                viewModel = vm,
                onFinish = {
                    vm.resetQuiz()
                    navController.popBackStack(Routes.HOME, false)
                }
            )
        }

        //  CHAT
        composable(Routes.CHAT) {
            UserInputSCR()
        }

        //  PROFILE
        composable(Routes.PROFILE) {
            UserProfileSCR(navController)
        }
    }
}