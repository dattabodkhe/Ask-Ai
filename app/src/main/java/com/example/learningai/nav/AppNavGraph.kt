package com.example.learningai.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningai.home.HomeSCR
import com.example.learningai.user.HomeScreen
import com.example.learningai.user.UserInputSCR
import com.example.learningai.user.UserProfileSCR

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier){

    NavHost(navController = navController,
        startDestination = Routes.HOME){
        composable(Routes.HOME){
            HomeScreen()
        }
        composable(Routes.CHAT){
            UserInputSCR()
        }
        composable(Routes.PROFILE){
            UserProfileSCR()
        }
    }
}