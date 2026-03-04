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

    // Universal Gradient
    val purpleGradient = Brush.horizontalGradient(
        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(purpleGradient, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .statusBarsPadding()
                    .padding(12.dp)
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            when (activeTab) {
                "MAIN" -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        SettingListTile("Account & Security", Icons.Default.Lock) { activeTab = "ACCOUNT_SECURITY" }
                        SettingListTile("Support & About", Icons.Default.Warning) { activeTab = "SUPPORT_ABOUT" }
                        SettingListTile("Social & Feedback", Icons.Default.Favorite) { activeTab = "SOCIAL_FEEDBACK" }

                        Spacer(Modifier.height(40.dp))

                        // LOGOUT CARD
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                auth.signOut()
                                navController.navigate(Routes.ROLE_SELECTION) { popUpTo(0) { inclusive = true } }
                            },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                        ) {
                            Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                                Spacer(Modifier.width(16.dp))
                                Text("Logout Account", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                "ACCOUNT_SECURITY" -> {
                    SettingsDetailCard(onBack = { activeTab = "MAIN" }) {
                        Text("Security Management", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(16.dp))

                        ActionRow("Reset Password via Email", Icons.Default.Lock) {
                            val email = auth.currentUser?.email
                            if (email != null) {
                                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }

                        HorizontalDivider(Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                        ActionRow("Delete My Account", Icons.Default.AccountBox, MaterialTheme.colorScheme.error) {
                            showDeleteDialog = true
                        }
                    }
                }

                "SUPPORT_ABOUT" -> {
                    SettingsDetailCard(onBack = { activeTab = "MAIN" }) {
                        Text("About LearningAI", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        Text("Version: 1.0.2", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        HorizontalDivider(Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        Text("Privacy Policy", fontWeight = FontWeight.Bold)
                        Text("Your data is safe with our encrypted servers.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                "SOCIAL_FEEDBACK" -> {
                    SettingsDetailCard(onBack = { activeTab = "MAIN" }) {
                        Text("Community & Feedback", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(12.dp))

                        ActionRow("Share with Friends", Icons.Default.Share) {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                val appLink = "https://play.google.com/store/apps/details?id=${context.packageName}"
                                putExtra(Intent.EXTRA_TEXT, "Hey! Check out LearningAI, it's amazing for students: $appLink")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        }

                        ActionRow("Follow us on Instagram", Icons.Default.AccountBox) {
                            showSocialSheet = true
                        }

                        ActionRow("Rate on Play Store", Icons.Default.ThumbUp) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
                            try { context.startActivity(intent) } catch (e: Exception) { }
                        }
                    }
                }
            }
        }
    }

    /* ---------- MODAL BOTTOM SHEET ---------- */
    if (showSocialSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSocialSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = Color(0xFFE1306C).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Star, null, modifier = Modifier.size(40.dp), tint = Color(0xFFE1306C))
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Follow Developer", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    "Get latest updates and AI tips directly from the developer.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        val instaUser = "app developer official"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$instaUser")).apply {
                            setPackage("com.instagram.android")
                        }
                        try { context.startActivity(intent) } catch (e: Exception) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$instaUser")))
                        }
                        scope.launch { sheetState.hide() }.invokeOnCompletion { showSocialSheet = false }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open Instagram", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }

    /* ---------- DIALOGS ---------- */
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { Text("All your progress will be permanently lost. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        auth.currentUser?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) navController.navigate(Routes.ROLE_SELECTION) { popUpTo(0) }
                            else Toast.makeText(context, "Please re-login to delete", Toast.LENGTH_SHORT).show()
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete", color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}

/* ---------- SHARED COMPONENTS ---------- */

@Composable
fun SettingListTile(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.AccountBox, null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun SettingsDetailCard(onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            content()
            Spacer(Modifier.height(24.dp))
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Back to Menu", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ActionRow(label: String, icon: ImageVector, color: Color = Color.Unspecified, onClick: () -> Unit) {
    val finalColor = if (color == Color.Unspecified) MaterialTheme.colorScheme.onSurface else color
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = finalColor.copy(alpha = 0.8f), modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, color = finalColor, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}