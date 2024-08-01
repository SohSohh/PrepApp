package com.example.myapplication.TestScreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.compose.PreperationAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingScreen(modifier: Modifier = Modifier,
                  testScreenViewModel: TestScreenViewModel = viewModel(),
                  onEndOfTest:() -> Unit = {},
                  onBackButtonOrGesture: () -> Unit = {},) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    BackHandler {
        onBackButtonOrGesture()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.White)
        ) {
            QuestionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                question = testScreenUiState.questions[testScreenUiState.currentQuestion],
                testScreenViewModel = testScreenViewModel,
                testScreenUiState = testScreenUiState,
                resultForm = false,
                resultAnswerIndex = 0,
                onEndOfTest = onEndOfTest
            )
        }
        Text(text = "[TESTING] CurrentQuestion = ${testScreenUiState.currentQuestion}")
        Text(text = "[TESTING] incorrectQuestions = ${testScreenUiState.incorrectQuestions}")
        Text(text = "[TESTING] answers = ${testScreenUiState.answers}")
        Text(text = "[TESTING] questions.size = ${testScreenUiState.questions.size}")
        Text(text = "[TESTING] selection = ${testScreenUiState.selection}")
        Text(text = "[TESTING] AllowSkipping = ${testScreenUiState.AllowSkipping}")
        Text(text = "[TESTING] Backtracking = ${testScreenUiState.Backtracking}")
        Text(text = "[TESTING] ShowCorrectAndIncorrect = ${testScreenUiState.ShowCorrectAndIncorrect}")

        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = modifier) {
            if (testScreenUiState.Backtracking && testScreenUiState.currentQuestion != 0) {
                NavigationButton(
                    modifier = modifier.padding(15.dp),
                    text = "Previous",
                    onClick = { testScreenViewModel.previousQuestion() })
            }
            Spacer(modifier = Modifier.weight(1f))
            // THIS SAYS: IF ALLOWSKIPPING IS TRUE -OR- RETRYQUESTION IS TRUE AND A CHOICE IS SELECTED, THEN SHOW THE BUTTON. THE LATTER PART IS SIMPLE TO LIMIT THE CARD FROM NAVIGATING TO A QUESTION THAT DOESN'T EXIST OVER THE LIMIT
            if (testScreenUiState.AllowSkipping) {
                if ((testScreenUiState.currentQuestion + 1) != (testScreenUiState.questions.size)) {
                    NavigationButton(
                        modifier = modifier.padding(15.dp),
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
                            } })
                } else {
                    NavigationButton(
                        modifier = modifier.padding(15.dp),
                        text = "End",
                        onClick = {
                            testScreenViewModel.checkAnswer()
                            testScreenViewModel.addAnswer() //WE NEED TO PERFORM THINGS THE NEW FUNCTION DID SEPAARTELY
                            testScreenUiState.selection = ""
                            onEndOfTest()})
                }
            }
        }
    }
}

val correctColor = Color.Green.copy(alpha = 0.5f)
val incorrectColor = Color.Red.copy(alpha = 0.5f)
val normalColor = Color.Transparent

@Composable 
fun QuestionCard(modifier:Modifier = Modifier,
                 question:question, testScreenViewModel: TestScreenViewModel,
                 resultForm:Boolean,
                 resultAnswerIndex: Int,
                 onEndOfTest: () -> Unit = {},
                 testScreenUiState: TestScreenUiState) {
    Card(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.background(color = Color.White)) {
                Text(text = question.subject.toString(),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.titleMedium)
                Text(text = question.question,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.titleLarge)
            Options(choiceList = question.choices,
                testScreenViewModel = testScreenViewModel,
                testScreenUiState = testScreenUiState,
                resultForm = resultForm,
                resultAnswerIndex = resultAnswerIndex,
                onEndOfTest = onEndOfTest)
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
            resultAnswerIndex:Int,) {
    choiceList.forEach { choice ->
        var color by remember { mutableStateOf(normalColor) }
        if ((testScreenUiState.selection != "" && testScreenUiState.ShowCorrectAndIncorrect && choice == testScreenUiState.questions[testScreenUiState.currentQuestion].answer) || (resultForm && choice == testScreenUiState.questions[resultAnswerIndex].answer)) {
            color = correctColor
        } else if((testScreenUiState.selection != "" && testScreenUiState.ShowCorrectAndIncorrect) || resultForm) {
            color = incorrectColor
        } else {
            color = normalColor
        }
        Row(modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .background(color = color),
            verticalAlignment = Alignment.CenterVertically) {
            val scope = rememberCoroutineScope()
            Text(text = choice, modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp))
            RadioButton(
                selected = ((!resultForm && choice == testScreenUiState.selection) ||
                        (resultForm && choice == testScreenUiState.answers[resultAnswerIndex])),//SOMETHING WRONG WITH THIS
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
                                testScreenViewModel.addAnswer() //WE NEED TO PERFORM THINGS THE NEW FUNCTION DID SEPAARTELY
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