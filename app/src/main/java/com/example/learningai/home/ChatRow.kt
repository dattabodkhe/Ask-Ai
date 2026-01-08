package com.example.learningai.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learningai.model.ChatItem

@Composable
fun ChatRow(
    chat: ChatItem,
    onClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔵 Profile circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.name.first().toString(),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            Text(
                text = chat.time,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )

            IconButton(onClick = onShareClick) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share Classroom"
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(start = 76.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
    }
}