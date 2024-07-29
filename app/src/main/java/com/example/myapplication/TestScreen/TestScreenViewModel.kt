package com.example.myapplication.TestScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    var Backtracking:Boolean = true,
    var AllowSkipping:Boolean = true,
    var ShowCorrectAndIncorrect:Boolean = false,
)
class TestScreenViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(TestScreenUiState())
    val uiState: StateFlow<TestScreenUiState> = _uiState.asStateFlow()
    fun toggleShowCorrectAndIncorrect() {
        _uiState.update { currentState ->
            currentState.copy(
                ShowCorrectAndIncorrect = !currentState.ShowCorrectAndIncorrect
            )
        }
    }

    fun toggleSkipping() {
        _uiState.update { currentState ->
            currentState.copy(
                AllowSkipping = !currentState.AllowSkipping
            )
        }
    }

    fun toggleBacktracking() {
        _uiState.update { currentState ->
            currentState.copy(
                Backtracking = !currentState.Backtracking
            )
        }
    }

    fun toggleTest() {
        _uiState.update { currentState ->
            currentState.copy(
                isStarted = !currentState.isStarted,
                currentQuestion = 0,
                answers = mutableListOf()
            )
        }
    }
    fun nextQuestion() {
        _uiState.update { currentState ->
            // variables and If condition is to limit the increment from going above the maximum
            var incrememnt:Int = 0
            var selection:String = currentState.selection
            if (currentState.currentQuestion != currentState.questions.size) {
                incrememnt = 1
                selection = ""
            }
            currentState.copy(
                currentQuestion = currentState.currentQuestion + incrememnt,
                selection = selection
            )
        }
    }
    fun previousQuestion() {
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestion = currentState.currentQuestion - 1
            )
        }
    }
    fun toggleRepeatPreviouslyAttemptedQuestions() {
        _uiState.update { currentState ->
            currentState.copy(
                repeatPreviouslyAttemptedQuestions = !currentState.repeatPreviouslyAttemptedQuestions
            )
        }
    }
    fun addAnswer(answer:String) {
        _uiState.update { currentState ->
            currentState.copy(
                answers = currentState.answers + answer,
                selection = answer
            )
        }
    }
    fun toggleRetry() {
        _uiState.update { currentState ->
            currentState.copy(
                RetryQuestions = !currentState.RetryQuestions
            )
        }
    }
    fun resetAnswersList() {
        _uiState.update { currentState ->
            currentState.copy(
                answers = mutableListOf()
            )
        }
    }
    fun checkAnswer(questionNumber:Int, answered:String) {
        _uiState.update { currentState ->
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