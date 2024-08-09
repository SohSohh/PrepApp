package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.dataAndNetwork.allQuestionsSet
import com.example.myapplication.dataAndNetwork.question
import com.example.myapplication.dataAndNetwork.subjects
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class TestScreenUiState(
    val isStarted:Boolean = false,
    val repeatPreviouslyAttemptedQuestions:Boolean = false,
    var questions:List<question> = mutableListOf(),
    var answers:List<String> = mutableListOf(),
    var selection:String = "",
    var currentQuestion:Int = 0,
    var currentSubjectIndex:Int = 0, // CHECK LINE 59 AND 133, IT'S THE INDEX IN THE ALLSUBJECTS LIST IT ENABLE NAVIGATION BETWEEN SUBJECTS
    var incorrectQuestions: List<Int> = mutableListOf(),
    var activeSubjectsList:List<Int> = mutableListOf(),
    //--------CONFIGURATION
    var Backtracking:Boolean = true,
    var AllowSkipping:Boolean = true,
    var ShowCorrectAndIncorrect:Boolean = true,
    //-----------SUBJECTS
    //----REMEMBER To UPDATE RESET FUNCTION WHEN ADDING NEW PROPERTIES
    var Physics:Int = 3,
    var Mathematics:Int = 2,
    var English:Int = 3,
    var Intelligence:Int = 0,
    var Computers:Int = 0,
    var Chemistry:Int = 0,
    var Biology:Int = 0,

    var allSubjectsQuestionsIndices:List<Int> = mutableListOf()
    // this is a list containing the index for the beginning of every subject
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
    suspend fun moveToNextSubject() {
        _uiState.update { currentState ->
            val newSubjectIndex = if ((currentState.currentSubjectIndex >= 0) && (currentState.currentSubjectIndex <= currentState.allSubjectsQuestionsIndices.size - 1)) {
                currentState.currentSubjectIndex + 1
            } else {
                currentState.currentSubjectIndex
            }
            val updatedAnswer = currentState.answers.toMutableList()
            val updatedIncorrectAnswers = currentState.incorrectQuestions.toMutableList()
            var selection = currentState.selection
            coroutineScope {
                launch {
                    for (i in currentState.currentQuestion until currentState.allSubjectsQuestionsIndices[newSubjectIndex]) {
                        if (i > updatedAnswer.size - 1) {
                            updatedAnswer.add(selection)
                        } else if (selection != "") {
                            updatedAnswer.apply {
                                this[i] = selection
                            }
                        }
                        if (i > updatedIncorrectAnswers.size - 1) {
                            updatedIncorrectAnswers.add(i)
                        }
                        selection = ""
                    }
                }
            }
            selection = if (currentState.allSubjectsQuestionsIndices[newSubjectIndex] < currentState.answers.size - 1) {
                currentState.answers[currentState.allSubjectsQuestionsIndices[newSubjectIndex]]
            } else {
                ""
            }



            currentState.copy(
                    currentQuestion = currentState.allSubjectsQuestionsIndices[newSubjectIndex], // MOVE TO THE INDEX THE NEXT SUBJECT IS IN
                    currentSubjectIndex = newSubjectIndex,
                    selection = selection,
                    answers = updatedAnswer,
                    incorrectQuestions = updatedIncorrectAnswers
                    )
        }
    }
    fun moveToPreviousSubject() {
        _uiState.update { currentState ->
            val newSubjectIndex = if ((currentState.currentSubjectIndex >= 0) && (currentState.currentSubjectIndex <= currentState.activeSubjectsList.size - 1)) {
                currentState.currentSubjectIndex - 1
            } else {
                currentState.currentSubjectIndex
            }
            val updatedAnswer = currentState.answers.toMutableList()
            if (currentState.currentQuestion > updatedAnswer.size - 1) {
                updatedAnswer.add(currentState.selection)
            } else {
                updatedAnswer.apply {
                    this[currentState.currentQuestion] = currentState.selection
                }
            }
            val selection =  currentState.answers[currentState.allSubjectsQuestionsIndices[newSubjectIndex]]

            currentState.copy(
                currentQuestion = currentState.allSubjectsQuestionsIndices[newSubjectIndex], // MOVE TO THE INDEX THE NEXT SUBJECT IS IN
                currentSubjectIndex = newSubjectIndex,
                selection = selection,
                answers = updatedAnswer
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
                answers = newAnswer,
            )
        }
    }

// THE SUBJECT NAVIGATOR SHOULD CHANGE WHEN YOU SKIP TO THE SUBJECTS MANUALLY, ALSO IT CRASHES IF YOU SKIP A SUBJECT AND THEN PRESS THE NEXT BUTTON TO GET TO THE LAST QUESTION
    fun nextQuestion() {
        _uiState.update { currentState ->
            val newAnswer = if (currentState.currentQuestion != currentState.answers.size) {
                currentState.answers.toMutableList().apply {
                    this[currentState.currentQuestion] = currentState.selection
                }
            } else{
                currentState.answers + currentState.selection
            }
            val updatedSubjectIndex = if (currentState.currentSubjectIndex != currentState.allSubjectsQuestionsIndices.size - 1 && currentState.currentQuestion + 1 >= currentState.allSubjectsQuestionsIndices[currentState.currentSubjectIndex + 1]) {
                currentState.currentSubjectIndex + 1
            } else {
                currentState.currentSubjectIndex
            }
            val selection = if (currentState.currentQuestion + 1 == newAnswer.size) {
                ""
            } else {
                newAnswer[currentState.currentQuestion + 1]
            }
            currentState.copy(
                currentQuestion = currentState.currentQuestion + 1,
                answers = newAnswer.toList(),
                selection = selection,
                currentSubjectIndex = updatedSubjectIndex
            )
        }
    }

    fun previousQuestion() {
        _uiState.update { currentState ->
            val newAnswer = if (currentState.currentQuestion != currentState.answers.size) {
                currentState.answers.toMutableList().apply {
                    this[currentState.currentQuestion] = currentState.selection
                }
            } else {
                currentState.answers + currentState.selection
            }
            val updatedSubjectIndex = if (currentState.currentSubjectIndex != 0 && currentState.currentQuestion - 1 < currentState.allSubjectsQuestionsIndices[currentState.currentSubjectIndex]) {
                currentState.currentSubjectIndex - 1
            } else {
                currentState.currentSubjectIndex
            }
            val selection = currentState.answers[currentState.currentQuestion - 1]
            currentState.copy(
                currentQuestion = currentState.currentQuestion - 1,
                currentSubjectIndex = updatedSubjectIndex,
                selection = selection,
                answers = newAnswer
            )
        }
    }

    fun toggleRepeatPreviouslyAttemptedQuestions() { //REDUNDANT, MIGHT BE IMPLEMENTED LATER
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
            // BELOW, WE INITIALIZE AN ACTIVE LIST TO REMOVE EVERY INSTANCE OF 0 FROM allSubjectQuantity SINCE THAT WOULD CAUSE REPETITION IN allSubjectQuestionsIndices
            val activeList:MutableList<Int> = mutableListOf()
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
                activeList.add(allSubjectQuantity[k])
                k += 1
            }

            // THIS VAL BELOW IS A LIST OF THE INDICES AT WHICH EACH SUBJECT BEGINS
            val allSubjectsQuestionIndices:MutableList<Int> = mutableListOf(0)
            for (i in 0 until (activeList.size - 1)) {
                val sum = if (i == 0) {
                    activeList[i]
                } else {
                    activeList.subList(0, i + 1).sum()
                }
                allSubjectsQuestionIndices.add(sum)
            }

            currentState.copy(
                questions = questionList,
                allSubjectsQuestionsIndices = allSubjectsQuestionIndices,
                activeSubjectsList = activeList
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

