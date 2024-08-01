package com.example.myapplication.TestScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme

@Composable
fun EndOfTestScreen(modifier:Modifier = Modifier,
                    testScreenViewModel: TestScreenViewModel = viewModel(),
                    onBackButtonOrGesture:() -> Unit = {}
                    ) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val totalQuestions = testScreenUiState.questions.size
    val correctQuestions = totalQuestions - testScreenUiState.incorrectQuestions.size
    BackHandler {
        onBackButtonOrGesture()
    }
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Text("You have completed the test!", style = MaterialTheme.typography.titleLarge)
      Text("You got ${correctQuestions} out of ${totalQuestions}", style = MaterialTheme.typography.titleLarge)
        Text(text = "Incorrect answers:", style = MaterialTheme.typography.titleMedium)
        Column(modifier = modifier) {
            testScreenUiState.incorrectQuestions.forEach() {
                QuestionCard(
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 10.dp)
                        .background(color = Color.Gray),
                    question = testScreenUiState.questions[it],
                    testScreenViewModel = testScreenViewModel,
                    testScreenUiState = testScreenUiState,
                    resultForm = true,
                    resultAnswerIndex = it,
                )
            }
        }
    }

}

@Preview
@Composable
fun EndPreview() {
    PreperationAppTheme {
        EndOfTestScreen()
    }
}