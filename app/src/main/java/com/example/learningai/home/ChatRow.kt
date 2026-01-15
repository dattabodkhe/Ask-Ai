package com.example.learningai.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learningai.model.ChatModel
import com.example.learningai.ui.theme.CardDark
import com.example.learningai.ui.theme.Purple

@Composable
fun ChatRow(
    chat: ChatModel,
    onClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardDark.copy(alpha = 0.95f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🔵 Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Purple),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chat.name.first().uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 📄 Name + last message
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.name,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = chat.lastMessage,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 🕒 Time + Share
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chat.time,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )

            IconButton(onClick = onShareClick) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }
    }
}