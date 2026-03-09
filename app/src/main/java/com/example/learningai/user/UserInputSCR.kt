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
import com.example.learningai.localDB.ChatMessageEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputSCR(
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(LocalContext.current)
    )
) {
    val state by viewModel.uiState.collectAsState()
    val historyGroups by viewModel.groupedHistory.collectAsState(initial = emptyMap())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var userText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll logic - Naye message par niche jayega
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                // History Header (Blue-Purple Gradient)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(Brush.horizontalGradient(listOf(Color(0xFF3B5BFF), Color(0xFF8A3FFC))))
                        .padding(20.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text("Chat History", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }

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

                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(historyGroups.keys.toList().sortedDescending()) { sessionId ->
                        val sessionMessages = historyGroups[sessionId] ?: emptyList()
                        val title = sessionMessages.firstOrNull { it.isUser }?.text ?: "Previous Chat"

                        NavigationDrawerItem(
                            label = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            selected = false,
                            onClick = {
                                viewModel.loadSession(sessionId, sessionMessages)
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.Menu, null, tint = Color(0xFF8A3FFC)) }
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AiChatHeader(
                    onBack = { navController.popBackStack() },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            // WindowInsets ko handle karna keyboard fix ke liye
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                // Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.messages) { msg ->
                        if (msg.isUser) {
                            UserBubble(msg.text)
                        } else {
                            val isLast = state.messages.lastOrNull() == msg
                            if (isLast && !state.isTyping && state.messages.size > 1) {
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

                // Modern Bottom Input Bar
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
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, enabled: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding() // Keyboard aate hi upar jayega
            .navigationBarsPadding(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(28.dp) // Pill Shape Look
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                enabled = enabled,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Ask AI Assistant...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 4
            )

            Spacer(Modifier.width(8.dp))

            // Premium Gradient Send Button
            IconButton(
                onClick = onSend,
                enabled = enabled && text.isNotBlank(),
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        brush = if (enabled && text.isNotBlank())
                            Brush.linearGradient(listOf(Color(0xFF3B5BFF), Color(0xFF8A3FFC)))
                        else
                            Brush.linearGradient(listOf(Color.LightGray, Color.Gray)),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
/* --- COMPONENTS FIXES --- */

@Composable
fun AiChatHeader(onBack: () -> Unit, onMenuClick: () -> Unit) {
    // Surface add kiya taaki status bar properly dikhe
    Surface(
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF3B5BFF), Color(0xFF8A3FFC))))
                .statusBarsPadding() // Status bar ke niche se shuru hoga
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, null, tint = Color.White) }
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("AI Assistant", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("History available in menu", color = Color.White.copy(0.7f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun UserBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(start = 60.dp), horizontalArrangement = Arrangement.End) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = 18.dp,
                bottomEnd = 2.dp
            ),
            color = Color(0xFF6C63FF),
            shadowElevation = 2.dp
        ) {
            Text(text = text, modifier = Modifier.padding(12.dp), color = Color.White)
        }
    }
}

@Composable
fun AiStaticBubble(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(end = 60.dp), horizontalArrangement = Arrangement.Start) {
        Surface(
            // AI bubble mein bottomStart ko small (2.dp) rakha hai
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomEnd = 18.dp,
                bottomStart = 2.dp
            ),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 1.dp
        ) {
            Text(text = text, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
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
fun TypingIndicator() {
    Text("AI is thinking...", color = Color.Gray, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp))
}

