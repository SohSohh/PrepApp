package com.example.myapplication.mcqPool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.TestScreen.QuestionCard
import com.example.myapplication.TestScreenViewModel
import com.example.myapplication.dataAndNetwork.allQuestionsSet

@Composable
fun McqPoolScreen(
    testScreenViewModel: TestScreenViewModel = viewModel(),
) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        allQuestionsSet.forEach() { set ->
            set.forEach() { question ->
                QuestionCard(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp).background(color = Color.Gray),
                    question = question,
                    testScreenViewModel = testScreenViewModel,
                    resultForm = true,
                    resultAnswerIndex = null,
                    testScreenUiState = testScreenUiState,
                    optionalAnswer = question.answer
                )
            }
        }
    }
}

@Preview
@Composable
fun MCQPoolPreview() {
    McqPoolScreen()
}