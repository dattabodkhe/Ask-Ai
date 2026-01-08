package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.learningai.model.ChatMessage
@Composable
fun UserInputSCR(navController: NavHostController) {

    var userText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()

    fun sendMessage() {
        if (userText.isNotBlank()) {
            messages = messages + ChatMessage(userText, true)
            messages = messages + ChatMessage("AI reply yahan aayega", false)
            userText = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "AskAI",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        /* ---------- CHAT LIST ---------- */
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                if (msg.isUser) {
                    UserMessageCard(message = msg.text)
                } else {
                    AiMessageCard(message = msg.text)
                }
            }
        }

        /* ---------- INPUT BAR ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()      // ✅ ONLY IME handling
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = userText,
                onValueChange = { userText = it },
                placeholder = { Text("Ask your question") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { sendMessage() })
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { sendMessage() }) {
                Text("Send")
            }
        }
    }

    /* ---------- AUTO SCROLL ---------- */
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }
}