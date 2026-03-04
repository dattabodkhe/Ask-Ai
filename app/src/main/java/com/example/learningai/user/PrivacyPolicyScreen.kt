package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    val purpleGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        /* --- Custom Header --- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = purpleGradient,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .statusBarsPadding(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Privacy Policy",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        /* --- Policy Content --- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Last Updated: March 2026",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            PolicySection(
                title = "1. Information Collection",
                content = "We collect basic information such as your Name, Email address, and Quiz performance data. This information is used to maintain your profile and calculate your 'Police Rank' within the app."
            )

            PolicySection(
                title = "2. Use of Data",
                content = "Your data is used exclusively to provide and improve our educational services. We do not sell, trade, or share your personal information with third-party organizations for marketing purposes."
            )

            PolicySection(
                title = "3. Security & Storage",
                content = "We utilize Google Firebase services to ensure your data is stored securely. We implement industry-standard encryption protocols to protect your information from unauthorized access."
            )

            PolicySection(
                title = "4. User Rights & Data Deletion",
                content = "You have the right to access or delete your personal data. Upon account deletion, all associated information, including quiz history and profile details, will be permanently removed from our database."
            )

            PolicySection(
                title = "5. Contact Us",
                content = "If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us at support@learningai.com."
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Terms & Conditions apply.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
@Composable
fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = content,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = Color(0xFF4B5563)
        )
        // Light divider for clean look
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }
}