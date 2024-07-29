package com.example.myapplication.TestScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


data class TestScreenUiState(
    val isStarted:Boolean = false,
    val repeatPreviouslyAttemptedQuestions:Boolean = false,
    var questions:List<question> = questionsList,
    var answers:List<String> = mutableListOf(),
    var selection:String = "",
    var currentQuestion:Int = 1,
    var incorrectQuestions:List<Int> = mutableListOf(),
    var RetryQuestions:Boolean = false,
    var Backtracking:Boolean = false,
    val AllowSkipping:Boolean = false,
)
class TestScreenViewModel:ViewModel() {
    val uiState = MutableStateFlow(TestScreenUiState())

    fun toggleSkipping() {
        uiState.update { currentState ->
            currentState.copy(
                AllowSkipping = !currentState.AllowSkipping
            )
        }
    }

    fun toggleBacktracking() {
        uiState.update { currentState ->
            currentState.copy(
                Backtracking = !currentState.Backtracking
            )
        }
    }

    fun toggleTest() {
        uiState.update { currentState ->
            currentState.copy(
                isStarted = !currentState.isStarted,
                currentQuestion = 0,
                answers = mutableListOf()
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
    fun toggleRetry() {
        uiState.update { currentState ->
            currentState.copy(
                RetryQuestions = !currentState.RetryQuestions
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
    fun checkAnswer(questionNumber:Int, answered:String) {
        uiState.update { currentState ->
            val isCorrect = currentState.questions[questionNumber].answer == answered
            val updatedIncorrectQuestions = if (isCorrect) {
                currentState.incorrectQuestions
            } else {
                currentState.incorrectQuestions + questionNumber
            }
            currentState.copy(
                incorrectQuestions = updatedIncorrectQuestions
            )
        }
        }
}