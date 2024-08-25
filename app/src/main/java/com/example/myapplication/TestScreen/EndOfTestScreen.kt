package com.example.myapplication.TestScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EndOfTestScreen(modifier:Modifier = Modifier,
                    testScreenViewModel: TestScreenViewModel = viewModel(),
                    onBackButtonOrGesture:() -> Unit = {}
                    ) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val totalQuestions = testScreenUiState.questions.size
    val correctQuestions = totalQuestions - testScreenUiState.incorrectQuestions.size
    var displayValue by remember { mutableStateOf(0) }

    BackHandler {
        onBackButtonOrGesture()
        testScreenViewModel.enableBars()
    }
    LaunchedEffect(key1 = Unit) {
        for (i in 0..3) {
            displayValue += 1
            delay(800)
        }
    }
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                AnimatedVisibility(
                    visible = displayValue >= 1,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 4 })
                ) {
                    Text(
                        text = "Test complete",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 15.dp)
                    )
                }

        }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                AnimatedVisibility(
                    visible = displayValue >= 2,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 4 })
                ) {
                    Text(
                        text = "Score: ${correctQuestions}/${totalQuestions}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }

            AnimatedVisibility(visible = displayValue >= 3,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 4 })
            ) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigationButton(
                        text = "Return",
                        modifier = Modifier.padding(top = 2.5f.dp),
                        onClick = onBackButtonOrGesture
                    )
                    LazyColumn() {
                        items(count = testScreenUiState.incorrectQuestions.size,
                            key = { it },
                            itemContent = {
                        QuestionCard(
                            modifier = Modifier
                                .padding(vertical = 20.dp, horizontal = 10.dp),
                            question = testScreenUiState.questions[it],
                            testScreenViewModel = testScreenViewModel,
                            testScreenUiState = testScreenUiState,
                            resultForm = true,
                            resultAnswerIndex = it,
                            optionalAnswer = "..."
                        )
                            })
                    }
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
@Preview
@Composable
fun EndDPreview() {
    PreperationAppTheme(true) {
        EndOfTestScreen()
    }
}