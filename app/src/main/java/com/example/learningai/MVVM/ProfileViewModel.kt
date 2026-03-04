package com.example.learningai.MVVM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.localDB.AppDatabase
import com.example.learningai.localDB.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userDao =
        AppDatabase
            .getDatabase(application)
            .userDao()


    private val _user =
        MutableStateFlow<UserEntity?>(null)

    val user: StateFlow<UserEntity?> = _user


    init {
        loadUser()
    }


    private fun loadUser() {

        viewModelScope.launch {

            _user.value =
                userDao.getUser()
        }
    }
}
