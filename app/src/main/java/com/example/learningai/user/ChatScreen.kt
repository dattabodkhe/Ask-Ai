package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningai.model.ChatMessage
import com.example.learningai.ui.theme.appGradient

@Composable
fun ChatScreen() {

    var userText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hi 👋 Ask me anything!", false)
        )
    }

    val listState = rememberLazyListState()

    fun sendMessage() {
        if (userText.isNotBlank()) {
            messages.add(ChatMessage(userText, true))
            userText = ""
            messages.add(
                ChatMessage("This is where AI response will come 🚀", false)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(
                    text = msg.text,
                    isUser = msg.isUser
                )
            }
        }

        ChatInputBar(
            text = userText,
            onTextChange = { userText = it },
            onSend = { sendMessage() }
        )
    }
}