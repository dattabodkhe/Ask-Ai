package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.ChatDao
import com.example.learningai.localDB.ChatMessageEntity
import com.example.learningai.model.ChatMessage
import com.example.learningai.model.ChatUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel(
    private val aiRepository: AiRepository,
    private val chatDao: ChatDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(messages = emptyList()))
    val uiState: StateFlow<ChatUiState> = _uiState

    private var currentSessionId = System.currentTimeMillis()

    val groupedHistory: Flow<Map<Long, List<ChatMessageEntity>>> = chatDao.getAllMessages()
        .map { messages -> messages.groupBy { it.sessionId } }

    fun sendMessage(text: String) {
        if (text.isBlank() || _uiState.value.isLimitExceeded) return

        _uiState.update { it.copy(messages = it.messages + ChatMessage(text, true), isTyping = true) }

        viewModelScope.launch {
            chatDao.insertMessage(ChatMessageEntity(sessionId = currentSessionId, text = text, isUser = true))

            try {
                val reply = aiRepository.chat(text)
                chatDao.insertMessage(ChatMessageEntity(sessionId = currentSessionId, text = reply, isUser = false))

                _uiState.update { it.copy(messages = it.messages + ChatMessage(reply, false), isTyping = false) }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun loadSession(sessionId: Long, messages: List<ChatMessageEntity>) {
        currentSessionId = sessionId
        val loadedMessages = messages.map { ChatMessage(it.text, it.isUser) }
        _uiState.update { it.copy(messages = loadedMessages) }
    }

    fun startNewChat() {
        currentSessionId = System.currentTimeMillis()
        _uiState.update { ChatUiState(messages = emptyList()) }
    }

    private fun handleError(e: Exception) {
        val msg = e.message ?: ""
        _uiState.update { it.copy(isTyping = false, isLimitExceeded = msg.contains("429") || msg.contains("limit", true)) }
    }

    fun dismissLimitPopup() { _uiState.update { it.copy(isLimitExceeded = false) } }
}