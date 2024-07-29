package com.example.myapplication.TestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestConfigurationScreen(
    modifier: Modifier = Modifier,
    testScreenViewModel: TestScreenViewModel = viewModel(),
    onStartButtonClicked: () -> Unit = {},
) {
    val testScreenUiState by testScreenViewModel._uiState.collectAsState()
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Box(
            modifier = modifier
                .background(color = Color.White)
        ) {
            ConfigurationsList(modifier = Modifier.padding(10.dp), testScreenUiState = testScreenUiState, testScreenViewModel = testScreenViewModel)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row() {
            Spacer(modifier = Modifier.weight(1f))
            NavigationButton(text = "Start", onClick = onStartButtonClicked, modifier = Modifier.padding(15.dp))
        }
    }
}

@Composable
fun NavigationButton(modifier: Modifier = Modifier, text:String, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun ConfigurationsList(modifier:Modifier = Modifier,
                       testScreenViewModel: TestScreenViewModel = viewModel(),
                       testScreenUiState: TestScreenUiState, ) {
    Column(modifier = modifier) {
        // This might be needed to be implemented later
        TextWithSwitch(text = "Include attempted questions from previous tests",
            checkedState = testScreenUiState.repeatPreviouslyAttemptedQuestions,
            onCheckChange = { testScreenViewModel.toggleRepeatPreviouslyAttemptedQuestions() })
        Text(text = testScreenUiState.repeatPreviouslyAttemptedQuestions.toString())
        //------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //--------
        TextWithSwitch(text = "Allow questions to be reattempted",
            checkedState = testScreenUiState.RetryQuestions,
            onCheckChange = { testScreenViewModel.toggleRetry() })
        //---------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //------
        TextWithSwitch(text = "Allow backtracking",
            checkedState = testScreenUiState.Backtracking,
            onCheckChange = { testScreenViewModel.toggleBacktracking() })
        //-----
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----
        TextWithSwitch(text = "Allow skipping",
            checkedState = testScreenUiState.AllowSkipping,
            onCheckChange = { testScreenViewModel.toggleSkipping() })
        //-----------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //----------
        TextWithSwitch(text = "Show correct answers",
            checkedState = testScreenUiState.ShowCorrectAndIncorrect,
            onCheckChange = { testScreenViewModel.toggleShowCorrectAndIncorrect() })
        Text(text = testScreenUiState.RetryQuestions.toString())
        Text(text = testScreenUiState.Backtracking.toString())
        Text(text = testScreenUiState.AllowSkipping.toString())
        Text(text = testScreenUiState.ShowCorrectAndIncorrect.toString())
    }
}


@Composable
fun TextWithSwitch(modifier:Modifier = Modifier, text:String, checkedState:Boolean, onCheckChange:(Boolean) -> Unit = {}) {
    Row(modifier = modifier.padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            modifier = Modifier.weight(5f)
        )
        Switch(
            checked = checkedState,
            onCheckedChange = onCheckChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PreviewApp() {
    PreperationAppTheme {
        TestConfigurationScreen()
    }
}