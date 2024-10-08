package com.example.myapplication

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dataAndNetwork.Api
import com.example.myapplication.dataAndNetwork.allQuestionsSet
import com.example.myapplication.dataAndNetwork.question
import com.example.myapplication.dataAndNetwork.subjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.properties.Delegates


data class TestScreenUiState(
    val isStarted:Boolean = false,
    val repeatPreviouslyAttemptedQuestions:Boolean = false,
    var questions:List<question> = mutableListOf(),
    var answers:List<String> = mutableListOf(),
    var limitList:List<Int> = mutableListOf(-1,-1,-1,-1,-1,-1,-1),
    var selection:String = "",
    var currentQuestion:Int = 0,
    var currentSubjectIndex:Int = 0, // CHECK LINE 59 AND 133, IT'S THE INDEX IN THE ALLSUBJECTS LIST IT ENABLE NAVIGATION BETWEEN SUBJECTS
    var incorrectQuestions: List<Int> = mutableListOf(),
    var activeSubjectsList:List<Int> = mutableListOf(),
    var Totaltime:Int = 10, //3 hours
    //--------CONFIGURATION
    var Backtracking:Boolean = true,
    var AllowSkipping:Boolean = true,
    var ShowCorrectAndIncorrect:Boolean = true,
    //-----------SUBJECTS
    //----REMEMBER To UPDATE RESET FUNCTION WHEN ADDING NEW PROPERTIES
    var Physics:Int = 3,
    var Mathematics:Int = 3,
    var English:Int = 2,
    var Intelligence:Int = 0,
    var Computers:Int = 0,
    var Chemistry:Int = 0,
    var Biology:Int = 0,

    var allSubjectsQuestionsIndices:List<Int> = mutableListOf(),
    // this is a list containing the index for the beginning of every subject

    //STATES
    var showBars:Boolean = true,
    var showBottomBar:Boolean = false,
    var currentScreen:String = "TestConfigurationScreen",
    var eligibleForTest:Boolean = true,
)
class TestScreenViewModel():ViewModel() {
    private val _uiState = MutableStateFlow(TestScreenUiState())
    val uiState: StateFlow<TestScreenUiState> = _uiState.asStateFlow()


    val toggleShowCorrectAndIncorrect = {
        _uiState.update { currentState ->
            currentState.copy(
                ShowCorrectAndIncorrect = !currentState.ShowCorrectAndIncorrect
            )
        }
    }


    val toggleSkipping = {
        _uiState.update { currentState ->
            currentState.copy(
                AllowSkipping = !currentState.AllowSkipping
            )
        }
    }

    val toggleBacktracking = {
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
    fun assignTimer(time:Int) {
        _uiState.update {currentState ->
            currentState.copy(
                Totaltime = time
            )
        }
    }

// THE SUBJECT NAVIGATOR SHOULD CHANGE WHEN YOU SKIP TO THE SUBJECTS MANUALLY, ALSO IT CRASHES IF YOU SKIP A SUBJECT AND THEN PRESS THE NEXT BUTTON TO GET TO THE LAST QUESTION
    fun nextQuestion() {
        _uiState.update { currentState ->
            lateinit var newAnswer:List<String>
            var updatedSubjectIndex by Delegates.notNull<Int>()
            lateinit var selection: String
            var currentQuestion by Delegates.notNull<Int>()
            if (currentState.currentQuestion != currentState.questions.size - 1) {
                newAnswer = if (currentState.currentQuestion != currentState.answers.size) {
                    currentState.answers.toMutableList().apply {
                        this[currentState.currentQuestion] = currentState.selection
                    }
                } else {
                    currentState.answers + currentState.selection
                }
                updatedSubjectIndex =
                    if (currentState.currentSubjectIndex != currentState.allSubjectsQuestionsIndices.size - 1 && currentState.currentQuestion + 1 >= currentState.allSubjectsQuestionsIndices[currentState.currentSubjectIndex + 1]) {
                        currentState.currentSubjectIndex + 1
                    } else {
                        currentState.currentSubjectIndex
                    }
                selection = if (currentState.currentQuestion + 1 == newAnswer.size) {
                    ""
                } else {
                    newAnswer[currentState.currentQuestion + 1]
                }
                currentQuestion = currentState.currentQuestion + 1
            } else {
                newAnswer = currentState.answers
                updatedSubjectIndex = currentState.currentSubjectIndex
                selection = currentState.selection
                currentQuestion = currentState.currentQuestion
            }
            currentState.copy(
                currentQuestion = currentQuestion,
                answers = newAnswer.toList(),
                selection = selection,
                currentSubjectIndex = updatedSubjectIndex
            )
        }
    }
    fun previousQuestion() {
        _uiState.update { currentState ->
            lateinit var newAnswer:List<String>
            var updatedSubjectIndex by Delegates.notNull<Int>()
            lateinit var selection: String
            var currentQuestion by Delegates.notNull<Int>()

            if (currentState.currentQuestion != 0) {
                newAnswer = if (currentState.currentQuestion != currentState.answers.size) {
                    currentState.answers.toMutableList().apply {
                        this[currentState.currentQuestion] = currentState.selection
                    }
                } else {
                    currentState.answers + currentState.selection
                }
                updatedSubjectIndex =
                    if (currentState.currentSubjectIndex != 0 && currentState.currentQuestion - 1 < currentState.allSubjectsQuestionsIndices[currentState.currentSubjectIndex]) {
                        currentState.currentSubjectIndex - 1
                    } else {
                        currentState.currentSubjectIndex
                    }
                selection = currentState.answers[currentState.currentQuestion - 1]
                currentQuestion = currentState.currentQuestion - 1
            } else {
                newAnswer = currentState.answers
                updatedSubjectIndex = currentState.currentSubjectIndex
                selection = currentState.selection
                currentQuestion = currentState.currentQuestion
            }
            currentState.copy(
                currentQuestion = currentQuestion,
                currentSubjectIndex = updatedSubjectIndex,
                selection = selection,
                answers = newAnswer
            )
        }
    }
    fun setScreen(text:String) {
        _uiState.update {currentState ->
            currentState.copy(
                currentScreen = text
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
    suspend fun initializeLimits() {
        val limits = Api.getLimits().awaitResponse()
        _uiState.update{
            currentState->
            currentState.copy(
                limitList = limits
            )
        }
    }

    fun initializeQuestions() {
        _uiState.update { currentState ->
            var questionList = mutableListOf<question>()
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
            val activeList = allSubjectQuantity.filter { it != 0 }
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
            // THIS VAL BELOW IS A LIST OF THE INDICES AT WHICH EACH SUBJECT BEGINS
            val allSubjectsQuestionIndices:MutableList<Int> = mutableListOf(0)
            for (i in (0 until activeList.size - 1)) {
                allSubjectsQuestionIndices.add(activeList.subList(0, i + 1).sum())
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
                    currentSubjectIndex = 0,
                    activeSubjectsList = mutableListOf()
                    //--------CONFIGURATION
                    //-----------SUBJECTS
                )
            }
        }
        fun outOfTime() {
            _uiState.update{currentState ->
                val size = currentState.questions.size
                val incorrectQuestions = currentState.incorrectQuestions.toMutableList()
                val answers = currentState.answers.toMutableList()
                for (i in (answers.size) until size) {
                    answers.add(element = "")
                    incorrectQuestions.add(element = i)
                }
                currentState.copy(
                    answers = answers,
                    incorrectQuestions = incorrectQuestions
                )
            }
        }
        fun enableBars() {
            _uiState.update { currentState ->
                currentState.copy(
                    showBars = true

                )
            }
        }
        fun disableBars() {
            _uiState.update { currentState ->
                currentState.copy(
                    showBars = false
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
    // THIS FUNCTION IS TO HANDLE ANY API CALLS FROM RETROFIT!!
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun <T> Call<T>.awaitResponse(): T {
        return suspendCancellableCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()!!)
                    } else {
                        continuation.resumeWithException(Exception("API call failed with error code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })

            continuation.invokeOnCancellation {
                try {
                    cancel()
                } catch (e: Exception) {
                    // Ignore cancellation exceptions
                }
            }
        }
    }
    //Get limits for each subjects
    fun getLimits() {
        var limitList = emptyList<Int>()
        viewModelScope.launch {
            try {
                val response = Api.getLimits().awaitResponse()
                    limitList = response
            } catch (e: Exception) {
                //Ignore
            }
            _uiState.update {
                current ->
                current.copy(
                    limitList = limitList
                )
            }
        }
    }
    }

