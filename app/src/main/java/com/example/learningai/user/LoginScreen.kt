package com.example.learningai.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.learningai.MVVM.AuthViewModel
import com.example.learningai.MVVM.AuthState
import com.example.learningai.nav.Routes
import com.example.learningai.ui.theme.*

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔥 APP TITLE
            Text(
                text = "Learning AI",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login to continue",
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 📧 EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                    focusedLabelColor = NeonBlue,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                    cursorColor = NeonBlue,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔒 PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                    focusedLabelColor = NeonBlue,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                    cursorColor = NeonBlue,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🚀 LOGIN BUTTON
            Button(
                onClick = {
                    // TEMP LOGIN (design dekhne ke liye)
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonBlue
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔗 FOOTER
            Text(
                text = "By continuing, you agree to our policies",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}