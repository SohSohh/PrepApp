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
    var currentQuestion:Int = 0,
    var incorrectQuestions:MutableList<Int> = mutableListOf(),
    //--------CONFIGURATION
    var RetryQuestions:Boolean = false,
    var Backtracking:Boolean = false,
    var AllowSkipping:Boolean = false,
    var ShowCorrectAndIncorrect:Boolean = false,
    //-----------SUBJECTS
    var physicsQ:Int = 0,
    var mathsQ:Int = 0,
    var englishQ:Int = 0,
    var intelligenceQ:Int = 0,
    var computerQ:Int = 0,
    var chemistryQ:Int = 0,
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
            val newAnswer = if (currentState.currentQuestion != currentState.answers.size) {
                currentState.answers.toMutableList().apply {
                    this[currentState.currentQuestion] = currentState.selection
                }
            } else {
                currentState.answers + currentState.selection
            }
            currentState.copy(
                currentQuestion = currentState.currentQuestion + 1,
                answers = newAnswer,
                selection = ""
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
    fun changeSelectionTo(answer:String) {
        _uiState.update { currentState ->
            currentState.copy(
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
    fun checkAnswer() {
        _uiState.update { currentState ->
            val isCorrect = currentState.selection == currentState.questions[currentState.currentQuestion].answer
            val updatedIncorrectQuestions = currentState.incorrectQuestions.toMutableList() // Create a mutable copy

            if (isCorrect && updatedIncorrectQuestions.contains(currentState.currentQuestion)) {
                updatedIncorrectQuestions.remove(currentState.currentQuestion) // Remove the current question if it was corrected
            } else if (!isCorrect && !updatedIncorrectQuestions.contains(currentState.currentQuestion)) {
                updatedIncorrectQuestions.add(currentState.currentQuestion) // Add to the list if incorrect
            }

            currentState.copy(
                incorrectQuestions = updatedIncorrectQuestions
            )
        }
    }
}