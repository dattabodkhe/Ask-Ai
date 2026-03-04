package com.example.learningai.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.learningai.model.Contact
import com.example.learningai.premission.getContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ContactsScreen(navController: NavHostController) {

    val context = LocalContext.current

    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var permissionGranted by remember { mutableStateOf(false) }
    var selectedContacts by remember { mutableStateOf<List<Contact>>(emptyList()) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            permissionGranted = granted
        }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            isLoading = true
            contacts = withContext(Dispatchers.IO) {
                getContacts(context)
            }
            isLoading = false
        }
    }

    val filteredContacts = remember(contacts, searchText) {
        if (searchText.isBlank()) contacts
        else contacts.filter {
            it.name.contains(searchText, true) ||
                    it.number.contains(searchText)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            /* ---------- HEADER ---------- */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF4F46E5),
                                Color(0xFF7C3AED)
                            )
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = 28.dp,
                            bottomEnd = 28.dp
                        )
                    )
                    .padding(20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "Add Friends",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ---------- SEARCH ---------- */

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search name or number") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                shape = RoundedCornerShape(18.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            when {

                !permissionGranted -> {
                    PermissionUI {
                        permissionLauncher.launch(
                            Manifest.permission.READ_CONTACTS
                        )
                    }
                }

                isLoading -> {
                    LoadingUI()
                }

                filteredContacts.isEmpty() -> {
                    EmptyUI()
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredContacts) { contact ->

                            val isSelected =
                                selectedContacts.contains(contact)

                            ModernContactItem(
                                contact = contact,
                                isSelected = isSelected,
                                onSelect = {

                                    selectedContacts =
                                        if (isSelected)
                                            selectedContacts - contact
                                        else
                                            selectedContacts + contact
                                }
                            )
                        }
                    }
                }
            }
        }

        /* ---------- BOTTOM ADD BUTTON ---------- */

        if (selectedContacts.isNotEmpty()) {

            Button(
                onClick = {
                    // TODO: Save to Firestore
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7)
                )
            ) {
                Text(
                    "Add (${selectedContacts.size}) Friends",
                    color = Color.White
                )
            }
        }
    }
}

/* ---------- CONTACT ITEM ---------- */

@Composable
fun ModernContactItem(
    contact: Contact,
    isSelected: Boolean,
    onSelect: (Contact) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFFE0E7FF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contact.name.first().uppercase(),
                    color = Color(0xFF4F46E5)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(contact.name)
                Text(
                    contact.number,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = { onSelect(contact) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF6C5CE7)
                )
            )
        }
    }
}
@Composable
fun LoadingUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF6C5CE7)
        )
    }
}
@Composable
fun EmptyUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No contacts found 😢",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}
@Composable
fun PermissionUI(onAllow: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Contacts Permission Required",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "Allow access to sync your friends.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onAllow,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C5CE7)
                    )
                ) {
                    Text("Allow Permission")
                }
            }
        }
    }
}