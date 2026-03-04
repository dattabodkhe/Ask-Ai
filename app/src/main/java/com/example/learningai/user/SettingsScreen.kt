package com.example.learningai.user

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learningai.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // UI States
    var activeTab by remember { mutableStateOf("MAIN") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Bottom Sheet State for Instagram Popup
    val sheetState = rememberModalBottomSheetState()
    var showSocialSheet by remember { mutableStateOf(false) }

    val purpleGradient = Brush.horizontalGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED)))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
    ) {
        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(purpleGradient, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (activeTab == "MAIN") navController.popBackStack()
                    else activeTab = "MAIN"
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
                Text(
                    text = if (activeTab == "MAIN") "Settings" else activeTab.replace("_", " "),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        /* ---------- CONTENT AREA ---------- */
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (activeTab) {
                "MAIN" -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        SettingListTile("Account & Security", Icons.Default.Lock) { activeTab = "ACCOUNT_SECURITY" }
                        SettingListTile("Support & About", Icons.Default.Info) { activeTab = "SUPPORT_ABOUT" }
                        SettingListTile("Social & Feedback", Icons.Default.Star) { activeTab = "SOCIAL_FEEDBACK" }

                        Spacer(Modifier.height(30.dp))

                        // LOGOUT CARD
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                auth.signOut()
                                navController.navigate(Routes.ROLE_SELECTION) { popUpTo(0) { inclusive = true } }
                            },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Delete, null, tint = Color.Red)
                                Spacer(Modifier.width(16.dp))
                                Text("Logout Account", color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                "ACCOUNT_SECURITY" -> {
                    SettingsDetailCard(onBack = { activeTab = "MAIN" }) {
                        Text("Security Management", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(16.dp))

                        ActionRow("Reset Password via Email", Icons.Default.Build) {
                            val email = auth.currentUser?.email
                            if (email != null) {
                                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }

                        HorizontalDivider(Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

                        ActionRow("Delete My Account", Icons.Default.Delete, Color.Red) {
                            showDeleteDialog = true
                        }
                    }
                }

                "SUPPORT_ABOUT" -> {
                    SettingsDetailCard(onBack = { activeTab = "MAIN" }) {
                        Text("About LearningAI", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Version: 1.0.2", color = Color.Gray, fontSize = 14.sp)
                        HorizontalDivider(Modifier.padding(vertical = 16.dp))
                        Text("Privacy Policy", fontWeight = FontWeight.Bold)
                        Text("Your data is safe with our Firebase-encrypted servers.", fontSize = 13.sp, color = Color.Gray)
                    }
                }

                "SOCIAL_FEEDBACK" -> {
                    SettingsDetailCard(onBack = { activeTab = "MAIN" }) {
                        Text("Community & Feedback", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(12.dp))

                        ActionRow("Share with Friends", Icons.Default.Share) {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                val appLink = "https://play.google.com/store/apps/details?id=${context.packageName}"
                                putExtra(Intent.EXTRA_TEXT, "Hey! Check out LearningAI, it's amazing for students: $appLink")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        }

                        // INSTAGRAM POPUP TRIGGER
                        ActionRow("Follow us on Instagram", Icons.Default.Info) {
                            showSocialSheet = true
                        }

                        ActionRow("Rate on Play Store", Icons.Default.Star) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
                            try { context.startActivity(intent) } catch (e: Exception) { }
                        }
                    }
                }
            }
        }
    }

    /* ---------- INSTAGRAM BOTTOM SHEET (POPUP) ---------- */
    if (showSocialSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSocialSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Instagram Brand Color Icon
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFE1306C)
                )
                Spacer(Modifier.height(16.dp))
                Text("Follow the Developer", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Get the latest updates and AI learning tips directly from the developer.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        val instaUser = "APP DEVELOPER"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$instaUser")).apply {
                            setPackage("com.instagram.android")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Browser backup if app is not installed
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$instaUser")))
                        }
                        // Close sheet after clicking
                        scope.launch { sheetState.hide() }.invokeOnCompletion { showSocialSheet = false }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Open Instagram", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    /* ---------- DELETE DIALOG ---------- */
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { Text("Are you sure? All your data will be permanently removed.") },
            confirmButton = {
                Button(
                    onClick = {
                        auth.currentUser?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) navController.navigate(Routes.ROLE_SELECTION) { popUpTo(0) }
                            else Toast.makeText(context, "Please re-login to delete", Toast.LENGTH_SHORT).show()
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Delete", color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}

/* ---------- SHARED COMPONENTS (STAYS SAME) ---------- */

@Composable
fun SettingListTile(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color(0xFF4F46E5), modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Star, null, tint = Color.LightGray)
        }
    }
}

@Composable
fun SettingsDetailCard(onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            content()
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F9))
            ) { Text("Back to Menu", color = Color.Black) }
        }
    }
}

@Composable
fun ActionRow(label: String, icon: ImageVector, color: Color = Color.Black, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = color.copy(alpha = 0.7f), modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, color = color, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}