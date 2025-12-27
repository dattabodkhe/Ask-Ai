package com.example.learningai.admin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    fun checkAdmin() {
        val email = FirebaseAuth.getInstance().currentUser?.email
        _isAdmin.value = email == "dattabodke77@gmail.com"
    }
}