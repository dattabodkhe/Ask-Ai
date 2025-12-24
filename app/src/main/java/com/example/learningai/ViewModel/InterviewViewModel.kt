package com.example.learningai.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.model.Questions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class InterviewViewModel : ViewModel(){
    private val questions = listOf(
        Questions(
            question = "What is jetpack compose?",
            option = listOf("Ui toolkit",
                "Api","DataBase","Complier"), correctAnswerIndex = 0
        )
    )

    private val _currentIndex = MutableStateFlow(0)

    val currentIndex : StateFlow<Int> = _currentIndex

    private val  _selectedOption = MutableStateFlow(-1)
    val selectedOption : StateFlow<Int> = _selectedOption

    fun selectedOption(index : Int){
        _selectedOption.value = index
    }
    fun nextQuestion(){
        _currentIndex.value = _currentIndex.value + 1
        _selectedOption.value = -1
    }
    val currentQuestion : Questions
        get() = questions[_currentIndex.value]


    fun  isCorrectOption(index: Int): Boolean{
        return index == questions[_currentIndex.value].correctAnswerIndex
    }
}