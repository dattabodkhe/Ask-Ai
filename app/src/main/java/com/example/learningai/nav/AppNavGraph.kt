package com.example.learningai.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learningai.home.HomeSCR
import com.example.learningai.home.InterviewOptionButton
import com.example.learningai.home.InterviewScreen
import com.example.learningai.user.UserInputSCR
import com.example.learningai.user.UserProfileSCR
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {

        composable(Routes.HOME) {
            HomeSCR(navController)
        }
        composable(Routes.INTERVIEW){
            InterviewScreen()
        }

        composable(Routes.CHAT) {
            UserInputSCR()
        }

        composable(Routes.PROFILE) {
            UserProfileSCR()
        }
    }
}