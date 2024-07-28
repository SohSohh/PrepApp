package com.example.myapplication.TestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme

@Composable
fun TestingScreen(modifier: Modifier = Modifier, testScreenViewModel: TestScreenViewModel = viewModel() ) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    Box(modifier = modifier.fillMaxSize().background(color = Color.Gray)) {
        QuestionCard(
            modifier = Modifier.fillMaxWidth(),
            question = testScreenUiState.questions[testScreenUiState.currentQuestion],
            testScreenViewModel = testScreenViewModel
        )
    }
}

@Composable 
fun QuestionCard(modifier:Modifier = Modifier, question:question, testScreenViewModel: TestScreenViewModel) {
    Card(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column() {
            Text(text = question.question, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge)
            Options(choiceList = question.choices, testScreenViewModel = testScreenViewModel)
        }
    }
}

@Composable
fun Options(modifier: Modifier = Modifier, choiceList:List<String>, testScreenViewModel: TestScreenViewModel) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    choiceList.forEach { choice ->
        Row(modifier = Modifier.padding(5.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = choice, modifier = Modifier.weight(1f).padding(horizontal = 20.dp))
            RadioButton(selected = (choice == testScreenUiState.selection), onClick = {
                testScreenViewModel.addAnswer(choice)
                testScreenViewModel.nextQuestion()
            },modifier = Modifier.padding(horizontal = 20.dp))
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