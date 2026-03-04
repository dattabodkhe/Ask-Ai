package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.learningai.classroom.*
import com.example.learningai.home.*
import com.example.learningai.login.*
import com.example.learningai.premission.*
import com.example.learningai.user.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues? = null
) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination =
        if (currentUser != null) Routes.HOME
        else Routes.ROLE_SELECTION

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues ?: PaddingValues())
    ) {

        /* ---------------- AUTH FLOW ---------------- */

        composable(Routes.ROLE_SELECTION) {
            RoleSelectionScreen { role ->
                navController.navigate("${Routes.LOGIN}/$role") {
                    popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                }
            }
        }

        composable(
            route = "${Routes.LOGIN}/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) {
            LoginRoute(
                authViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        /* ---------------- HOME ---------------- */

        composable(Routes.HOME) {
            HomeDashboardScreen(navController)
        }

        /* ---------------- CHAT ---------------- */

        composable(Routes.CHAT) {
            UserInputSCR(navController)
        }

        /* ---------------- CLASSROOM ---------------- */

        composable(Routes.CLASSROOM) {
            ClassroomScreen(navController)
        }

        composable(Routes.JOIN_CLASSROOM) {
            JoinClassroomScreen(navController)
        }

        composable(
            route = "${Routes.CLASSROOM_CHAT}/{classId}",
            arguments = listOf(navArgument("classId") { type = NavType.StringType })
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            ClassroomChatScreen(navController, classId)
        }

        composable(
            route = "group_profile/{classId}",
            arguments = listOf(navArgument("classId") { type = NavType.StringType })
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            GroupProfileScreen(navController, classId)
        }

        /* ---------------- QUESTIONS SCREEN ---------------- */

        composable(
            route = "${Routes.QUESTIONSCREEN}/{classroomId}/{subject}/{count}/{difficulty}",
            arguments = listOf(
                navArgument("classroomId") { type = NavType.StringType },
                navArgument("subject") { type = NavType.StringType },
                navArgument("count") { type = NavType.IntType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val classroomId =
                backStackEntry.arguments?.getString("classroomId") ?: ""
            val subject =
                backStackEntry.arguments?.getString("subject") ?: ""
            val count =
                backStackEntry.arguments?.getInt("count") ?: 0
            val difficulty =
                backStackEntry.arguments?.getString("difficulty") ?: ""

            QuestionsScreen(
                navController = navController,
                classroomId = classroomId,
                subject = subject,
                count = count,
                difficulty = difficulty
            )
        }

        /* ---------------- RESULT SCREEN ---------------- */

        composable(
            route = "${Routes.RESULT}/{classroomId}/{score}/{totalQuestions}/{userId}",
            arguments = listOf(
                navArgument("classroomId") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType },
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val classroomId =
                backStackEntry.arguments?.getString("classroomId") ?: ""
            val score =
                backStackEntry.arguments?.getInt("score") ?: 0
            val totalQuestions =
                backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            val userId =
                backStackEntry.arguments?.getString("userId") ?: ""

            ResultScreen(
                classroomId = classroomId,
                score = score,
                totalQuestions = totalQuestions,
                userId = userId,
                navController = navController
            )
        }

        /* ---------------- PROFILE ---------------- */

        composable(Routes.USER_PROFILE) {
            UserProfileSCR(navController)
        }

        /* ---------------- SETTINGS ---------------- */

        composable(Routes.SETTINGS) {
            SettingsScreen(navController)
        }

        /* ---------------- PRIVACY POLICY ---------------- */

        composable(Routes.PRIVACY_POLICY) {
            PrivacyPolicyScreen(navController)
        }

        /* ---------------- CONTACTS ---------------- */

        composable(Routes.CONTACTS) {
            ContactsScreen(navController)
        }

        /* ---------------- AI QUESTION CREATION ---------------- */

        composable(Routes.CREATE_AI_QUESTION) {
            CreateAIquestion(navController)
        }

        composable(
            route = "${Routes.SELECT_CLASSROOM}/{questionsJson}",
            arguments = listOf(
                navArgument("questionsJson") { type = NavType.StringType }
            )
        ) { entry ->
            val json = entry.arguments?.getString("questionsJson") ?: ""
            SelectClassroomScreen(navController, json)
        }

        composable(
            route = "${Routes.PREVIEW_QUESTIONS}/{classId}/{questionsJson}",
            arguments = listOf(
                navArgument("classId") { type = NavType.StringType },
                navArgument("questionsJson") { type = NavType.StringType }
            )
        ) { entry ->
            val classId = entry.arguments?.getString("classId") ?: ""
            val json = entry.arguments?.getString("questionsJson") ?: ""
            PreviewQuestionsScreen(navController, classId, json)
        }
    }
}