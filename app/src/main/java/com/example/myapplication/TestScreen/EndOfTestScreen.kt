package com.example.myapplication.TestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme

@Composable
fun EndOfTestScreen(modifier:Modifier = Modifier,
                    testScreenViewModel: TestScreenViewModel = viewModel(),
                    ) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    var totalQuestions = testScreenUiState.currentQuestion + 1
    var correctQuestions = totalQuestions - testScreenUiState.incorrectQuestions.size
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Text("You have completed the test!", style = MaterialTheme.typography.titleLarge)
        Text("You got ${correctQuestions} out of ${totalQuestions}", style = MaterialTheme.typography.titleLarge)
    }
}

@Preview
@Composable
fun EndPreview() {
    PreperationAppTheme {
        EndOfTestScreen()
    }
}