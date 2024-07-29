package com.example.myapplication.TestScreen

import android.annotation.SuppressLint
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

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingScreen(modifier: Modifier = Modifier,
                  testScreenViewModel: TestScreenViewModel = viewModel(),
                  onClickEndTest:() -> Unit = {}) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
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
                modifier = Modifier.fillMaxWidth(),
                question = testScreenUiState.questions[testScreenUiState.currentQuestion],
                testScreenViewModel = testScreenViewModel,
                qNumber = testScreenUiState.currentQuestion
            )
        }
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
            if (((testScreenUiState.AllowSkipping || (testScreenUiState.selection != "" && testScreenUiState.RetryQuestions == true)) && (testScreenUiState.currentQuestion + 1) != (testScreenUiState.questions.size))) {
                NavigationButton(
                    modifier = modifier.padding(15.dp),
                    text = "Next",
                    onClick = { testScreenViewModel.nextQuestion() })
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
                 qNumber: Int) {
    Card(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Text(text = question.question, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge)
            Options(choiceList = question.choices, testScreenViewModel = testScreenViewModel, qNumber = qNumber, answer = question.answer)
        }
    }
}

@Composable
fun Options(modifier: Modifier = Modifier,
            choiceList:List<String>,
            testScreenViewModel: TestScreenViewModel,
            answer:String, qNumber:Int,
            color:Color = normalColor) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    choiceList.forEach { choice ->
        var color by remember { mutableStateOf(normalColor) }
        if (testScreenUiState.selection != "" && choice == testScreenUiState.questions[qNumber].answer && testScreenUiState.ShowCorrectAndIncorrect) {
            color = correctColor
        } else if(testScreenUiState.selection != "" && testScreenUiState.ShowCorrectAndIncorrect) {
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
                selected = (choice == testScreenUiState.selection),
                onClick = {
                    testScreenViewModel.addAnswer(choice)
                    testScreenViewModel.checkAnswer(qNumber, choice)
                    if (testScreenUiState.RetryQuestions == false && testScreenUiState.AllowSkipping == false) {
                        scope.launch {
                            delay(300)
                            testScreenViewModel.nextQuestion()
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