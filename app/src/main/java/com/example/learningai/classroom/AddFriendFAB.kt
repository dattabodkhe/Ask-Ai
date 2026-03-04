package com.example.learningai.classroom

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun AddFriendFAB(
    onContactsAllowed: () -> Unit
) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {
            onContactsAllowed()
        }
    }

    FloatingActionButton(
        onClick = {

            val permission = Manifest.permission.READ_CONTACTS

            if (
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                onContactsAllowed()

            } else {

                launcher.launch(permission)
            }
        }
    ) {

        Icon(Icons.Default.Person, contentDescription = "Add Friend")

    }
}
