package com.example.learningai.mvvm

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.MVVM.AddQuestionsViewModel
import com.example.learningai.ai.AiRepository
import com.example.learningai.localDB.AppDatabase

class AddQuestionsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)
        return AddQuestionsViewModel(
            AiRepository(context),
            db.questionDao() as Application
        ) as T
    }
}