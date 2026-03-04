package com.example.learningai.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.MVVM.DetailsFormViewModel
import com.example.learningai.classroom.*
import com.example.learningai.home.*
import com.example.learningai.login.*
import com.example.learningai.model.UserRole
import com.example.learningai.repository.DetailsFormViewModelFactory
import com.example.learningai.user.*
import com.example.learningai.premission.SelectClassroomScreen
import com.example.learningai.premission.PreviewQuestionsScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@Composable
fun AppNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues? = null
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) Routes.HOME else Routes.ROLE_SELECTION

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues ?: PaddingValues())
    ) {

        /* ---------- AUTH & ROLE SELECTION ---------- */
        composable(Routes.ROLE_SELECTION) {
            RoleSelectionScreen { role ->
                navController.navigate("${Routes.LOGIN}/$role") {
                    popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                }
            }
        }

        composable("${Routes.LOGIN}/{role}") { entry ->
            val role = entry.arguments?.getString("role") ?: "SELF"
            LoginRoute(
                authViewModel = viewModel(),
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        /* ---------- ONBOARDING / DETAILS FORM ---------- */
        composable("${Routes.DETAILS_FORM}/{role}") { entry ->
            val roleString = entry.arguments?.getString("role") ?: "SELF"
            val role = try { UserRole.valueOf(roleString) } catch (e: Exception) { UserRole.SELF }
            val vm: DetailsFormViewModel = viewModel(factory = DetailsFormViewModelFactory(role))
            val uiState by vm.uiState.collectAsState()

            DetailsFormScreen(
                uiState = uiState,
                onInstitutionTypeSelected = { vm.onInstitutionTypeSelected(it) },
                onCountryChanged = { vm.onCountryChanged(it) },
                onStateChanged = { vm.onStateChanged(it) },
                onUniversityChanged = { vm.onUniversityChanged(it) },
                onCollegeChanged = { vm.onCollegeChanged(it) },
                onCollegeEmailChanged = { vm.onCollegeEmailChanged(it) },
                onCollegeIdChanged = { vm.onCollegeIdChanged(it) },
                onPrnChanged = { vm.onPrnChanged(it) },
                onPrivateClassChanged = { vm.onPrivateClassChanged(it) },
                onStudentIdChanged = { vm.onStudentIdChanged(it) },
                onSubmit = {
                    vm.onSubmit()
                    navController.navigate(Routes.HOME) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        /* ---------- MAIN DASHBOARD & PROFILE ---------- */
        composable(Routes.HOME) { HomeDashboardScreen(navController) }

        composable(Routes.USER_PROFILE) { UserProfileSCR(navController) }

        // Settings Route
        composable(Routes.SETTINGS) { SettingsScreen(navController) }

        composable(Routes.PRIVACY_POLICY) { PrivacyPolicyScreen(navController) }

        composable(Routes.CONTACTS) { ContactsScreen(navController) }

        /* ---------- CLASSROOM FLOW ---------- */
        composable(Routes.CLASSROOM) { ClassroomScreen(navController) }

        composable(Routes.CREATE_CLASSROOM) { CreateClassroomSCR(navController) }

        composable(Routes.JOIN_CLASSROOM) { JoinClassroomScreen(navController) }

        composable("${Routes.CLASSROOM_CHAT}/{classId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            ClassroomChatScreen(navController, classId)
        }

        /* ---------- AI & CHAT ---------- */
        composable(Routes.CHAT) { UserInputSCR(navController) }

        composable(Routes.CREATE_AI_QUESTION) { CreateAIquestion(navController) }

        /* ---------- QUESTION FLOW ---------- */
        composable("${Routes.SELECT_CLASSROOM}/{questionsJson}") { backStackEntry ->
            val questionsJson = backStackEntry.arguments?.getString("questionsJson") ?: ""
            SelectClassroomScreen(navController = navController, questionsJson = questionsJson)
        }

        composable("${Routes.PREVIEW_QUESTIONS}/{classId}/{questionsJson}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val questionsJson = backStackEntry.arguments?.getString("questionsJson") ?: ""
            val questionsList = try {
                Gson().fromJson(questionsJson, Array<String>::class.java).toList()
            } catch (e: Exception) { emptyList<String>() }

            PreviewQuestionsScreen(navController = navController, initialQuestions = questionsList, selectedClassId = classId)
        }

        composable("${Routes.QUESTIONSCREEN}/{classroomId}/{subject}/{count}/{difficulty}") { backStackEntry ->
            val count = backStackEntry.arguments?.getString("count")?.toIntOrNull() ?: 0
            QuestionsScreen(
                navController = navController,
                classroomId = backStackEntry.arguments?.getString("classroomId") ?: "",
                subject = backStackEntry.arguments?.getString("subject") ?: "",
                count = count,
                difficulty = backStackEntry.arguments?.getString("difficulty") ?: "EASY"
            )
        }

        /* ---------- RESULTS ---------- */
        composable("${Routes.RESULT}/{classId}/{score}/{total}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("classId") ?: ""
            val s = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val t = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0
            ResultScreen(id, s, t, FirebaseAuth.getInstance().currentUser?.uid ?: "", navController)
        }
    }
}