package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.learningai.ui.theme.CardDark
import com.example.learningai.ui.theme.NeonBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardDark)
            .padding(8.dp)
            .imePadding(), // ✅ ONLY HERE
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text("Ask AI something…", color = Color.LightGray)
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = NeonBlue,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        IconButton(onClick = onSend) {
            Icon(Icons.Default.Send, null, tint = NeonBlue)
        }
    }
}