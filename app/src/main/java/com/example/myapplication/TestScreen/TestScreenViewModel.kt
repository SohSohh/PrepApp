package com.example.myapplication.TestScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


data class TestScreenUiState(
    val isStarted:Boolean = false,
    val attemptedQuestions:Int = 0,
    val repeatPreviouslyAttemptedQuestions:Boolean = false,
    var totalQuestions:Int = 200,
    var questions:List<question> = questionsList,
    var answers:List<String> = mutableListOf(),
    var selection:String = "",
    var currentQuestion:Int = 0
)
class TestScreenViewModel:ViewModel() {
    val uiState = MutableStateFlow(TestScreenUiState())

    fun toggleTest() {
        uiState.update { currentState ->
            currentState.copy(
                isStarted = !currentState.isStarted
            )
        }
    }
    fun nextQuestion() {
        uiState.update { currentState ->
            currentState.copy(
                currentQuestion = currentState.currentQuestion + 1
            )
        }
    }
    fun previousQuestion() {
        uiState.update { currentState ->
            currentState.copy(
                currentQuestion = currentState.currentQuestion - 1
            )
        }
    }
    fun toggleRepeatPreviouslyAttemptedQuestions() {
        uiState.update { currentState ->
            currentState.copy(
                repeatPreviouslyAttemptedQuestions = !currentState.repeatPreviouslyAttemptedQuestions
            )
        }
    }
    fun addAnswer(answer:String) {
        uiState.update { currentState ->
            currentState.copy(
                answers = currentState.answers + answer,
                selection = answer
            )
        }
    }
    fun resetAnswersList() {
        uiState.update { currentState ->
            currentState.copy(
                answers = mutableListOf()
            )
        }
    }
}