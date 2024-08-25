package com.example.myapplication.mcqPool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreen.QuestionCard
import com.example.myapplication.TestScreenViewModel
import com.example.myapplication.dataAndNetwork.question

@Composable
fun McqPoolScreen(
    testScreenViewModel: TestScreenViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    modifier:Modifier = Modifier,
) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val searchText by searchViewModel.searchText.collectAsState()
    val visibleQuestions by searchViewModel.visibleQuestions.collectAsState()
    if (visibleQuestions == emptyList<List<question>>()) {
        searchViewModel.updateVisibleQuestion()
    }
    val currentKeyboard = LocalSoftwareKeyboardController.current
    val currentFocus = LocalFocusManager.current
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)) {
        TextField(value = searchText,
            onValueChange = searchViewModel::onQueryEntered,
            textStyle = (TextStyle(
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ) + MaterialTheme.typography.displayMedium),
            maxLines = 3,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
            ),
            label = { Text(text = "Search questions, subjects, or choices", modifier = Modifier.alpha(0.65f), style = MaterialTheme.typography.headlineMedium) },
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
        LazyColumn() {
            items(count = visibleQuestions.size,
                key =  { visibleQuestions[it].question },
                itemContent = { index ->
                    QuestionCard(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        question = visibleQuestions[index],
                        testScreenViewModel = testScreenViewModel,
                        resultForm = true,
                        resultAnswerIndex = null,
                        testScreenUiState = testScreenUiState,
                        optionalAnswer = visibleQuestions[index].answer,
                        resizable = true
                    )
                })
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

@Preview
@Composable
fun MCQPoolDPreview() {
    PreperationAppTheme(true) {
        McqPoolScreen()
    }
}