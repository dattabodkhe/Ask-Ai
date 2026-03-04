package com.example.learningai.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningai.model.DetailsFormUiState
import com.example.learningai.model.InstitutionType
import com.example.learningai.model.UserRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsFormViewModel(role: UserRole) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsFormUiState(role = role))
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<DetailsFormEvent>()
    val event = _event.asSharedFlow()

    fun onInstitutionTypeSelected(type: InstitutionType) {
        _uiState.update { it.copy(institutionType = type) } // Fixed: errorMessage removed
        validate()
    }

    fun onCountryChanged(value: String) {
        _uiState.update { it.copy(country = value, state = "") }
        validate()
    }

    fun onStateChanged(value: String) {
        _uiState.update { it.copy(state = value) }
        validate()
    }

    fun onUniversityChanged(value: String) {
        _uiState.update { it.copy(universityName = value) }
        validate()
    }

    fun onCollegeChanged(value: String) {
        _uiState.update { it.copy(collegeName = value) }
        validate()
    }

    fun onCollegeEmailChanged(value: String) {
        _uiState.update { it.copy(collegeEmail = value) }
        validate()
    }

    fun onCollegeIdChanged(value: String) {
        _uiState.update { it.copy(collegeId = value) } // Fixed: collegeIdCard changed to collegeId
        validate()
    }

    fun onPrnChanged(value: String) {
        _uiState.update { it.copy(prnNumber = value) }
        validate()
    }

    fun onPrivateClassChanged(value: String) {
        _uiState.update { it.copy(privateClassName = value) }
        validate()
    }

    fun onStudentIdChanged(value: String) {
        _uiState.update { it.copy(studentId = value) }
        validate()
    }

    fun onSubmit() {
        if (!_uiState.value.isSubmitEnabled) {
            viewModelScope.launch {
                _event.emit(DetailsFormEvent.ShowError("Please fill all required fields"))
            }
            return
        }
        viewModelScope.launch {
            _event.emit(DetailsFormEvent.NavigateNext)
        }
    }

    private fun validate() {
        val s = _uiState.value
        val locationValid = s.country.isNotBlank() && s.state.isNotBlank()

        val institutionValid = when (s.institutionType) {
            InstitutionType.COLLEGE -> {
                s.universityName.isNotBlank() && s.collegeName.isNotBlank() &&
                        when (s.role) {
                            UserRole.TEACHER -> s.collegeEmail.isNotBlank()
                            UserRole.STUDENT -> s.collegeId.isNotBlank() && s.prnNumber.isNotBlank() // Fixed here
                            UserRole.SELF -> true
                        }
            }
            InstitutionType.PRIVATE -> {
                s.privateClassName.isNotBlank() && (s.role != UserRole.STUDENT || s.studentId.isNotBlank())
            }
            InstitutionType.NONE -> s.role == UserRole.SELF
            InstitutionType.UNIVERSITY -> s.universityName.isNotBlank()
            InstitutionType.SCHOOL -> s.collegeName.isNotBlank()
        }

        val valid = locationValid && institutionValid
        _uiState.update { it.copy(isSubmitEnabled = valid) }
    }
}

sealed class DetailsFormEvent {
    object NavigateNext : DetailsFormEvent()
    data class ShowError(val message: String) : DetailsFormEvent()
}