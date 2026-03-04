package com.example.learningai.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.learningai.MVVM.DetailsFormEvent
import com.example.learningai.MVVM.DetailsFormViewModel
import com.example.learningai.login.DetailsFormScreen

@Composable
fun DetailsFormRoute(
    viewModel: DetailsFormViewModel,
    onNavigateNext: () -> Unit,
    onShowError: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.event.collect { event ->

            when (event) {

                is DetailsFormEvent.NavigateNext -> {
                    onNavigateNext()
                }

                is DetailsFormEvent.ShowError -> {
                    onShowError(event.message)
                }
            }
        }
    }

    DetailsFormScreen(

        uiState = uiState,

        onInstitutionTypeSelected =
            viewModel::onInstitutionTypeSelected,

        onCountryChanged =
            viewModel::onCountryChanged,

        onStateChanged =
            viewModel::onStateChanged,

        onUniversityChanged =
            viewModel::onUniversityChanged,

        onCollegeChanged =
            viewModel::onCollegeChanged,

        onCollegeEmailChanged =
            viewModel::onCollegeEmailChanged,

        onCollegeIdChanged =
            viewModel::onCollegeIdChanged,

        onPrnChanged =
            viewModel::onPrnChanged,

        onPrivateClassChanged =
            viewModel::onPrivateClassChanged,

        onStudentIdChanged =
            viewModel::onStudentIdChanged,

        onSubmit =
            viewModel::onSubmit
    )
}
