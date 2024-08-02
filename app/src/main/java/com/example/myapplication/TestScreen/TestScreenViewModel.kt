package com.example.myapplication.TestScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class TestScreenUiState(
    val isStarted:Boolean = false,
    val repeatPreviouslyAttemptedQuestions:Boolean = false,
    var questions:List<question> = mutableListOf(),
    var answers:List<String> = mutableListOf(),
    var selection:String = "",
    var currentQuestion:Int = 0,
    var incorrectQuestions: List<Int> = mutableListOf(),
    //--------CONFIGURATION
    var Backtracking:Boolean = false,
    var AllowSkipping:Boolean = false,
    var ShowCorrectAndIncorrect:Boolean = false,
    //-----------SUBJECTS
    //----REMEMBER TU UPDATE RESET FUNCTION WHEN ADDING NEW PROPERTIES
    var Physics:Int = 0,
    var Mathematics:Int = 0,
    var English:Int = 0,
    var Intelligence:Int = 0,
    var Computers:Int = 0,
    var Chemistry:Int = 0,
    var Biology:Int = 0,
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

    fun addAnswer() {
        _uiState.update { currentState ->
            val newAnswer = if (currentState.currentQuestion != currentState.answers.size) {
                currentState.answers.toMutableList().apply {
                    this[currentState.currentQuestion] = currentState.selection
                }
            } else {
                currentState.answers + (currentState.selection)
            }
            currentState.copy(
                answers = newAnswer
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
                answers = newAnswer.toList(),
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

    fun changeSelectionTo(answer: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selection = answer
            )
        }
    }

    fun initializeQuestions() {
        _uiState.update { currentState ->
            val questionList = mutableListOf<question>()
            val allSubjectQuantity = listOf(
                currentState.Physics,
                currentState.Mathematics,
                currentState.English,
                currentState.Intelligence,
                currentState.Computers,
                currentState.Chemistry,
                currentState.Biology
            )
            val addedQuestions = mutableSetOf<question>()
            var k = 0
            for (questionsSet in allQuestionsSet) {
                val numberOfQuestions = allSubjectQuantity[k]
                repeat(numberOfQuestions) {
                    var newQuestion: question?
                    do {
                        newQuestion = questionsSet.random()
                    } while (addedQuestions.contains(newQuestion))

                    if (newQuestion != null) {
                        questionList.add(newQuestion)
                        addedQuestions.add(newQuestion)
                    }
                }
                k += 1
            }

            currentState.copy(
                questions = questionList
            )
        }
    }

        fun assignQuestionsTo(subject: subjects, value: Int) {
            _uiState.update { currentState ->
                when (subject) {
                    subjects.Physics -> currentState.copy(Physics = value)
                    subjects.Mathematics -> currentState.copy(Mathematics = value)
                    subjects.Chemistry -> currentState.copy(Chemistry = value)
                    subjects.Biology -> currentState.copy(Biology = value)
                    subjects.English -> currentState.copy(English = value)
                    subjects.Intelligence -> currentState.copy(Intelligence = value)
                    subjects.Computers -> currentState.copy(Computers = value)
                }
            }
        }

        fun reset() {
            _uiState.update { currentState ->
                currentState.copy(
                    answers = mutableListOf(),
                    isStarted = false,
                    repeatPreviouslyAttemptedQuestions = false,
                    questions = mutableListOf(),
                    selection = "",
                    currentQuestion = 0,
                    incorrectQuestions = mutableListOf(),
                    //--------CONFIGURATION
                    Backtracking = false,
                    AllowSkipping = false,
                    ShowCorrectAndIncorrect = false,
                    //-----------SUBJECTS
                )
            }
        }

        fun checkAnswer() {
            _uiState.update { currentState ->
                val isCorrect =
                    (currentState.selection == currentState.questions[currentState.currentQuestion].answer)
                val updatedIncorrectQuestions =
                    currentState.incorrectQuestions.toMutableList() // Create a mutable copy

                if (isCorrect && updatedIncorrectQuestions.contains(currentState.currentQuestion)) {
                    updatedIncorrectQuestions.remove(currentState.currentQuestion) // Remove the current question if it was corrected
                } else if (!isCorrect && !updatedIncorrectQuestions.contains(currentState.currentQuestion)) {
                    updatedIncorrectQuestions.add(currentState.currentQuestion) // Add to the list if incorrect
                }

                currentState.copy(
                    incorrectQuestions = updatedIncorrectQuestions.toList()
                )
            }
        }
    }
