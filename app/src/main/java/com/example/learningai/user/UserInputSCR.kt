package com.example.learningai.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.learningai.MVVM.ChatViewModel
import com.example.learningai.MVVM.ChatViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ... (Top imports same rahenge)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputSCR(
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(LocalContext.current))
) {
    val state by viewModel.uiState.collectAsState()
    val historyGroups by viewModel.groupedHistory.collectAsState(initial = emptyMap())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var userText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll logic
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    // Drawer wrapper
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(320.dp),
                drawerContainerColor = Color(0xFFF6F7FB)
            ) {
                // Drawer Header
                Box(
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                        .background(Brush.horizontalGradient(listOf(Color(0xFF3B5BFF), Color(0xFF8A3FFC))))
                        .padding(20.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text("History", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }

                // New Chat Button
                NavigationDrawerItem(
                    label = { Text("+ New Chat", fontWeight = FontWeight.Bold, color = Color(0xFF3B5BFF)) },
                    selected = false,
                    onClick = {
                        viewModel.startNewChat()
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(8.dp)
                )
                HorizontalDivider()

                // History Sessions List
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(historyGroups.keys.toList().sortedDescending()) { sessionId ->
                        val sessionMessages = historyGroups[sessionId] ?: emptyList()
                        val title = sessionMessages.firstOrNull { it.isUser }?.text ?: "Purani Chat"

                        NavigationDrawerItem(
                            label = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            selected = false,
                            onClick = {
                                viewModel.loadSession(sessionId, sessionMessages)
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.Menu, null, tint = Color(0xFF8A3FFC)) },
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    ) {
        // Main Screen Scaffold inside Drawer
        Scaffold(
            topBar = {
                AiChatHeader(
                    onBack = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF6F7FB))
            ) {
                // Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.messages) { msg ->
                        if (msg.isUser) {
                            UserBubble(msg.text)
                        } else {
                            val isLast = state.messages.lastOrNull() == msg
                            if (isLast && !state.isTyping) {
                                AiStreamingBubble(msg.text, listState)
                            } else {
                                AiStaticBubble(msg.text)
                            }
                        }
                    }
                    if (state.isTyping) {
                        item { TypingIndicator() }
                    }
                }

                // Input Bar at bottom
                ChatInputBar(
                    text = userText,
                    onTextChange = { userText = it },
                    enabled = !state.isLimitExceeded,
                    onSend = {
                        if (userText.isNotBlank()) {
                            viewModel.sendMessage(userText)
                            userText = ""
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AiStreamingBubble(fullText: String, listState: androidx.compose.foundation.lazy.LazyListState) {
    var displayedText by remember { mutableStateOf("") }
    LaunchedEffect(fullText) {
        displayedText = ""
        fullText.split(" ").forEach { word ->
            displayedText += "$word "
            delay(40)
            if (listState.layoutInfo.totalItemsCount > 0) {
                listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
            }
        }
    }
    AiStaticBubble(displayedText)
}

@Composable
fun AiStaticBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Surface(shape = RoundedCornerShape(18.dp), color = Color.White, tonalElevation = 2.dp) {
            Text(text = text, modifier = Modifier.padding(12.dp).widthIn(max = 260.dp), color = Color.Black)
        }
    }
}

@Composable
fun AiChatHeader(onBack: () -> Unit, onMenuClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(Brush.horizontalGradient(listOf(Color(0xFF3B5BFF), Color(0xFF8A3FFC)))), contentAlignment = Alignment.CenterStart) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White) }
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White) }
            Column {
                Text("AI Assistant", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Swipe left for history", color = Color.White.copy(0.7f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun UserBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFF6C63FF)) {
            Text(text = text, modifier = Modifier.padding(12.dp).widthIn(max = 260.dp), color = Color.White)
        }
    }
}

@Composable
fun TypingIndicator() {
    Text("AI is thinking...", color = Color.Gray, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, enabled: Boolean) {
    Surface(tonalElevation = 8.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                enabled = enabled,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask AI...") },
                colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
            )
            IconButton(onClick = onSend, enabled = enabled && text.isNotBlank(), modifier = Modifier.background(if(enabled && text.isNotBlank()) Color(0xFF6C63FF) else Color.LightGray, CircleShape)) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
            }
        }
    }
}