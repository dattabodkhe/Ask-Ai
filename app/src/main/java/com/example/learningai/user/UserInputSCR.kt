package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.learningai.model.ChatMessage
import com.example.learningai.ui.theme.*
@Composable
fun UserInputSCR(
    navController: NavHostController
) {
    var userText by remember { mutableStateOf("") }

    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hi 👋 Ask me anything!", false)
        )
    }

    fun sendMessage() {
        if (userText.isNotBlank()) {
            messages.add(0, ChatMessage(userText, true))
            userText = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(
                    text = msg.text,
                    isUser = msg.isUser
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.ime.only(WindowInsetsSides.Bottom))
        ) {
            ChatInputBar(
                text = userText,
                onTextChange = { userText = it },
                onSend = { sendMessage() }
            )
        }
    }
}
@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = if (isUser)
                        Brush.horizontalGradient(listOf(Purple, NeonBlue))
                    else
                        Brush.verticalGradient(listOf(CardDark, DarkBlue)),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(text = text, color = Color.White)
        }
    }
}