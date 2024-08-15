package com.example.myapplication.mcqPool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreen.QuestionCard
import com.example.myapplication.TestScreenViewModel

@Composable
fun McqPoolScreen(
    testScreenViewModel: TestScreenViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    modifier:Modifier = Modifier,
) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val searchText by searchViewModel.searchText.collectAsState()
    val visibleQuestions by searchViewModel.visibleQuestions.collectAsState()

    val currentKeyboard = LocalSoftwareKeyboardController.current
    val currentFocus = LocalFocusManager.current
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .background(color = Color.Cyan)) {
        TextField(value = searchText,
            onValueChange = searchViewModel::onQueryEntered,
            textStyle = TextStyle(fontSize = 18.sp),
            maxLines = 3,
            label = { Text(text = "Search questions, subjects, or choices", modifier = Modifier.alpha(0.5f), style = MaterialTheme.typography.headlineMedium) },
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    currentKeyboard?.hide()
                    currentFocus.clearFocus()
                }
            ),
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) }
        )
        visibleQuestions.forEach() { question ->
                QuestionCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .background(color = Color.Gray),
                    question = question,
                    testScreenViewModel = testScreenViewModel,
                    resultForm = true,
                    resultAnswerIndex = null,
                    testScreenUiState = testScreenUiState,
                    optionalAnswer = question.answer,
                    resizable = true
                )
            }
        }
    }


@Preview
@Composable
fun MCQPoolPreview() {
    PreperationAppTheme {
        McqPoolScreen()
    }
}