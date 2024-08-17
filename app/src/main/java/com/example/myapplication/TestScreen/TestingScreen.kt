package com.example.myapplication.TestScreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreenUiState
import com.example.myapplication.TestScreenViewModel
import com.example.myapplication.dataAndNetwork.physicsQ
import com.example.myapplication.dataAndNetwork.question
import com.example.myapplication.dataAndNetwork.subjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer

@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingScreen(
    modifier: Modifier = Modifier,
    testScreenViewModel: TestScreenViewModel = viewModel(),
    onEndOfTest: () -> Unit = {},
    navController: NavController = rememberNavController(),
    onBackButtonOrGesture: () -> Unit = {},
) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    //TEST DUMMY VALUES
//    testScreenUiState.questions = physicsQ
//    testScreenUiState.currentQuestion = 1
    //-----------------------
    BackHandler {
        navController.popBackStack()
        testScreenViewModel.enableBars()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState()),
    ) {

        Column(
            modifier = Modifier
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TestTimer(testScreenUiState = testScreenUiState,
                onEndOfTest = onEndOfTest,
                modifier = Modifier.padding(vertical = 20.dp))

            QuestionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                question = testScreenUiState.questions[testScreenUiState.currentQuestion],
                testScreenViewModel = testScreenViewModel,
                testScreenUiState = testScreenUiState,
                resultForm = false,
                resultAnswerIndex = null,
                onEndOfTest = onEndOfTest,
                optionalAnswer = "..."
            )
        }

        //TESTS
//        Text(text = "[TESTING] CurrentQuestion = ${testScreenUiState.currentQuestion}")
//        Text(text = "[TESTING] incorrectQuestions = ${testScreenUiState.incorrectQuestions}")
//        Text(text = "[TESTING] answers = ${testScreenUiState.answers}")
//        Text(text = "[TESTING] questions.size = ${testScreenUiState.questions.size}")
//        Text(text = "[TESTING] selection = ${testScreenUiState.selection}")
//        Text(text = "[TESTING] AllowSkipping = ${testScreenUiState.AllowSkipping}")
//        Text(text = "[TESTING] Backtracking = ${testScreenUiState.Backtracking}")
//        Text(text = "[TESTING] ShowCorrectAndIncorrect = ${testScreenUiState.ShowCorrectAndIncorrect}")
//        Text(text = "[TESTING] CurrentSubjectIndex = ${testScreenUiState.currentSubjectIndex}")
//        Text(text = "[TESTING] allSubjectsQuestionsIndices = ${testScreenUiState.allSubjectsQuestionsIndices}")
//        Text(text = "[TESTING] questions = ${testScreenUiState.questions}")

        //----

//        Spacer(modifier = Modifier.weight(0.25f))

        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            //----PREVIOUS BUTTON
                    NavigationButton(
                        modifier = modifier.padding(10.dp),
                        text = "Previous",
                        onClick = {
                            if (testScreenUiState.ShowCorrectAndIncorrect) {
                                scope.launch {
                                    delay(150)
                                    testScreenViewModel.checkAnswer()
                                    testScreenViewModel.previousQuestion()
                                }
                            } else {
                                testScreenViewModel.checkAnswer()
                                testScreenViewModel.previousQuestion()
                            }
                        },
                        enabledCondition = (testScreenUiState.Backtracking && (testScreenUiState.currentQuestion != 0))
                    )

            //-----------------
            //---SUBJECT NAVIGATION BUTTON
                SubjectNavigationButton(
                    currentSubject = testScreenUiState.questions[testScreenUiState.currentQuestion].subject,
                    onForwardButton = {
                        scope.launch {
                            testScreenViewModel.moveToNextSubject()
                        }
                    },
                    onPrevButton = { testScreenViewModel.moveToPreviousSubject() },
                    modifier = Modifier.padding(10.dp),
                    conditionForPrevVisible = (testScreenUiState.currentSubjectIndex != 0 && testScreenUiState.Backtracking),
                    conditionForNextVisible = (testScreenUiState.currentSubjectIndex != testScreenUiState.activeSubjectsList.size - 1) && testScreenUiState.AllowSkipping
                )
                //--------------------
            //------ NEXT / END BUTTON
            // THIS (below) SAYS: IF ALLOWSKIPPING IS TRUE -OR- RETRYQUESTION IS TRUE AND A CHOICE IS SELECTED, THEN SHOW THE BUTTON. THE LATTER PART IS SIMPLE TO LIMIT THE CARD FROM NAVIGATING TO A QUESTION THAT DOESN'T EXIST OVER THE LIMIT
            if ((testScreenUiState.currentQuestion + 1) != (testScreenUiState.questions.size)) {
                    NavigationButton(
                        modifier = modifier.padding(10.dp),
                        text = "Next",
                        onClick = {
                            if (testScreenUiState.ShowCorrectAndIncorrect) {
                                scope.launch {
                                    delay(150)
                                    testScreenViewModel.checkAnswer()
                                    testScreenViewModel.nextQuestion()
                                }
                            } else {
                                testScreenViewModel.checkAnswer()
                                testScreenViewModel.nextQuestion()
                            } },
                        enabledCondition = (testScreenUiState.AllowSkipping))
                } else {
                    NavigationButton(
                        modifier = modifier.padding(10.dp),
                        text = "End",
                        onClick = {
                            testScreenViewModel.checkAnswer()
                            testScreenViewModel.addAnswer() //WE NEED TO PERFORM THINGS THE NEW FUNCTION DID SEPARTELY
                            testScreenUiState.selection = ""
                            onEndOfTest()
                        },
                        enabledCondition = (testScreenUiState.AllowSkipping))
                }
            //------------------------
        }
    }
}

val correctColor = Color.Green.copy(alpha = 0.5f)
val incorrectColor = Color.Red.copy(alpha = 0.5f)
val normalColor = Color.Transparent

@Composable 
fun QuestionCard(modifier:Modifier = Modifier,
                 question: question, testScreenViewModel: TestScreenViewModel,
                 resultForm:Boolean,
                 resultAnswerIndex: Int?,
                 onEndOfTest: () -> Unit = {},
                 optionalAnswer: String,
                 resizable:Boolean = false,
                 testScreenUiState: TestScreenUiState
) {
    var optionsVisible by remember { mutableStateOf(true) }
    if (resizable) {
        optionsVisible = false
    }
    Card(modifier = modifier.animateContentSize(), shape = MaterialTheme.shapes.medium, onClick = { if (resizable) { optionsVisible = !optionsVisible } }) {
        Column(modifier = Modifier.background(color = Color.White)) {
                Text(text = question.subject.toString(),
                    modifier = Modifier
                        .padding(start = 15.dp, end = 20.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall)
                Text(text = question.question,
                    modifier = Modifier
                        .padding(start = 15.dp, end = 20.dp, bottom = 10.dp)
                        .fillMaxWidth()
                        .animateContentSize(),
                    style = MaterialTheme.typography.bodyMedium)
            if (optionsVisible) {
                Options(
                    choiceList = question.choices,
                    testScreenViewModel = testScreenViewModel,
                    testScreenUiState = testScreenUiState,
                    resultForm = resultForm,
                    resultAnswerIndex = resultAnswerIndex,
                    onEndOfTest = onEndOfTest,
                    optionalAnswer = optionalAnswer
                )
            }
        }
    }
}

@Composable
fun TestTimer(testScreenUiState: TestScreenUiState, onEndOfTest: () -> Unit, modifier: Modifier = Modifier) {
    var timeValue by remember { mutableStateOf(testScreenUiState.Totaltime) }
    var hours = timeValue / 3600
    var minutes = timeValue % 3600 / 60
    var seconds = (timeValue % 3600)%60
    Card(modifier = modifier) {
        Text(
            text = "${hours.toString().padStart(2, '0')}:${
                minutes.toString().padStart(2, '0')
            }:${seconds.toString().padStart(2, '0')}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(10.dp)
        )
    }
    LaunchedEffect(key1 = Unit) {
        while(timeValue != 0) {
            delay(1000)
            timeValue -= 1
        }
    }
    if (timeValue == 0) {
        onEndOfTest()
    }
}

@Composable
fun SubjectNavigationButton(modifier:Modifier = Modifier,
                            currentSubject: subjects,
                            onPrevButton:() -> Unit = {},
                            onForwardButton:() -> Unit = {},
                            conditionForPrevVisible:Boolean = true,
                            conditionForNextVisible:Boolean = true) {
    Row(modifier = modifier) {
            Button(
                shape = RoundedCornerShape(
                    topStart = 15.dp,
                    topEnd = 0.dp,
                    bottomStart = 15.dp,
                    bottomEnd = 0.dp
                ),
                onClick = onPrevButton,
                modifier = Modifier.padding(end = 3.dp),
                enabled = conditionForPrevVisible
            ) {
                Text(text = "<", style = MaterialTheme.typography.displaySmall)
            }
            Button(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 15.dp
                ),
                onClick = onForwardButton,
                modifier = Modifier.padding(start = 3.dp),
                enabled = conditionForNextVisible
            ) {
                Text(text = ">", style = MaterialTheme.typography.displaySmall)
        }
    }
}
@Composable
fun Options(modifier: Modifier = Modifier,
            choiceList:List<String>,
            testScreenViewModel: TestScreenViewModel,
            testScreenUiState: TestScreenUiState,
            onEndOfTest: () -> Unit = {},
            resultForm:Boolean,
            resultAnswerIndex:Int?,
            optionalAnswer:String
            ) {
    choiceList.forEach { choice ->
        var color by remember { mutableStateOf(normalColor) }
        if ((testScreenUiState.selection != "" && testScreenUiState.ShowCorrectAndIncorrect && choice == testScreenUiState.questions[testScreenUiState.currentQuestion].answer && !resultForm) || (resultForm && (choice == (if (resultAnswerIndex != null) {testScreenUiState.questions[resultAnswerIndex].answer} else {optionalAnswer})))) {
            color = correctColor
        } else if((testScreenUiState.selection != "" && testScreenUiState.ShowCorrectAndIncorrect) || resultForm) {
            color = incorrectColor
        } else {
            color = normalColor
        }
        Row(modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .background(color = color, shape = RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically) {
            val scope = rememberCoroutineScope()

            Text(text = choice, modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp))
            RadioButton(
                selected = ((!resultForm && choice == testScreenUiState.selection) ||
                        (resultForm && (choice == (if (resultAnswerIndex != null) {testScreenUiState.answers[resultAnswerIndex]} else { optionalAnswer})))),//SOMETHING WRONG WITH THIS
                enabled = (!resultForm),
                onClick = {
                    if (choice != testScreenUiState.selection) {
                        testScreenViewModel.changeSelectionTo(choice)
                    }
                    if (!testScreenUiState.AllowSkipping) {
                        val proceedToNextQuestion = {
                            if ((testScreenUiState.currentQuestion + 1) != testScreenUiState.questions.size) {
                                testScreenViewModel.nextQuestion()
                            } else {
                                testScreenViewModel.addAnswer() //WE NEED TO PERFORM THINGS THE NEW FUNCTION DID SEPARTELY
                                testScreenUiState.selection = ""
                                onEndOfTest()
                            }
                        }

                        if (testScreenUiState.ShowCorrectAndIncorrect) {
                            scope.launch {
                                testScreenViewModel.checkAnswer()
                                delay(450)
                                proceedToNextQuestion()
                            }
                        } else {
                            testScreenViewModel.checkAnswer()
                            proceedToNextQuestion()
                        }
                    }
                },
            )
        }
    }

}


@Preview
@Composable
fun TestingScreenPreview() {
    PreperationAppTheme {
        TestingScreen()
    }
}