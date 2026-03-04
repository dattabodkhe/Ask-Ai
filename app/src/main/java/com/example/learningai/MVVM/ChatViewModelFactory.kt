package com.example.learningai.MVVM

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.AppDatabase

class ChatViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {

            // 1. AiRepository ko context de kar initialize kiya
            val aiRepository = AiRepository(context)

            // 2. Room Database ka instance liya aur uska chatDao nikala
            val database = AppDatabase.getDatabase(context)
            val chatDao = database.chatDao()

            // 3. ViewModel ko dono cheezein (Repository + DAO) pass kardi
            return ChatViewModel(aiRepository, chatDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}