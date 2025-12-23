package com.example.learningai.user



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import com.example.learningai.model.ChatMessage


@Composable
fun UserInputSCR() {

    var userText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()

    fun sendMessage(){
        if (userText.isNotBlank()){
            messages = messages + ChatMessage(userText,true)
            messages = messages + ChatMessage("AI reply", false)
            userText = ""
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {

        Header()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
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

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                keyboardActions = KeyboardActions(onSend = {
                    sendMessage()
                })
            )




            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (userText.isNotBlank()) {
                        messages = messages + ChatMessage(userText, true)
                        messages = messages + ChatMessage(
                            "AI reply yahan aayega",
                            false
                        )
                        userText = ""
                    }
                }
            ) {
                Text("Send")
            }

        }
    }
}
