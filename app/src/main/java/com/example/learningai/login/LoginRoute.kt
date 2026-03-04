package com.example.learningai.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.model.AuthState


@Composable
fun LoginRoute(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as? Activity

    val authState by authViewModel.authState.collectAsState()


    /* Init Google */
    LaunchedEffect(Unit) {

        authViewModel.initGoogle(context)
    }


    /* Google Launcher */
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                authViewModel.handleGoogleResult(
                    result.data
                ) { error ->

                    Toast.makeText(
                        context,
                        error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    /* Observe Login */
    LaunchedEffect(authState) {

        if (authState is AuthState.Success) {

            onLoginSuccess()
        }
    }


    /* UI */
    LoginScreen(

        onGoogleLogin = {

            if (activity == null) {

                Toast.makeText(
                    context,
                    "Activity not found",
                    Toast.LENGTH_SHORT
                ).show()

                return@LoginScreen
            }

            try {

                val intent =
                    authViewModel.getGoogleLoginIntent()

                launcher.launch(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    context,
                    "Google Login Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        },

        onEmailLogin = { email, password ->

            authViewModel.loginWithEmail(email, password)
        }
    )
}
