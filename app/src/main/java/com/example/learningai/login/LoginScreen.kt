package com.example.learningai.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.learningai.R
import com.example.learningai.ui.theme.Primary

@Composable
fun LoginScreen(
    onGoogleLogin: () -> Unit,
    onEmailLogin: (String, String) -> Unit
) {

    var showEmailForm by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF7F9FF),
                        Color(0xFFF1F4FF)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            /* Logo */
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Primary,
                modifier = Modifier.size(72.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.outline_question_mark_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(Modifier.height(20.dp))


            Text(
                "Welcome to LearningAI",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )


            Spacer(Modifier.height(6.dp))


            Text(
                "Sign in to continue your journey",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            Spacer(Modifier.height(36.dp))


            /* Google Button */
            Button(
                onClick = onGoogleLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {

                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text("Continue with Google")
            }


            Spacer(Modifier.height(22.dp))


            /* OR */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                HorizontalDivider(Modifier.weight(1f))

                Text("  or  ")

                HorizontalDivider(Modifier.weight(1f))
            }


            Spacer(Modifier.height(22.dp))


            /* Email Button */
            OutlinedButton(
                onClick = {
                    showEmailForm = !showEmailForm
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {

                Icon(Icons.Default.Email, null)

                Spacer(Modifier.width(12.dp))

                Text("Continue with Email")
            }


            /* Email Form */
            if (showEmailForm) {

                Spacer(Modifier.height(24.dp))


                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                Spacer(Modifier.height(14.dp))


                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )


                Spacer(Modifier.height(18.dp))


                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            onEmailLogin(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    Text("Login")
                }
            }
        }


        /* Bottom Text */
        Text(
            "By continuing, you agree to our Terms & Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        )
    }
}
