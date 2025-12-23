package com.example.learningai.user



import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.learningai.model.ChatMessage


@Composable
fun UserChat(messages: List<ChatMessage>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp),
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
}