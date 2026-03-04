package com.example.learningai.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningai.MVVM.DetailsFormViewModel
import com.example.learningai.model.UserRole

class DetailsFormViewModelFactory(
    private val role: UserRole
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DetailsFormViewModel::class.java)) {

            return DetailsFormViewModel(role) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
