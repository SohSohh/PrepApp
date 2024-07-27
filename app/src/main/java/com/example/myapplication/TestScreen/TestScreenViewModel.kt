package com.example.myapplication.TestScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


data class TestScreenUiState (
    val isStarted:Boolean = false,
    val attemptedQuestions:Int = 0,
    val repeatPreviouslyAttemptedQuestions:Boolean = false,
    var totalQuestions:Int = 200
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

    fun toggleRepeatPreviouslyAttemptedQuestions() {
        uiState.update { currentState ->
            currentState.copy(
                repeatPreviouslyAttemptedQuestions = !currentState.repeatPreviouslyAttemptedQuestions
            )
        }
    }
}