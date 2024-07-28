package com.example.myapplication.TestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compose.PreperationAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestConfigurationScreen(modifier:Modifier = Modifier,
                            testScreenViewModel: TestScreenViewModel = viewModel(), navController: NavController) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    Box(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        ConfigurationsList(modifier = Modifier.padding())
    }
}

@Composable
fun ConfigurationsList(modifier:Modifier = Modifier,
                       testScreenViewModel: TestScreenViewModel = viewModel()) {
    val testUiState by testScreenViewModel.uiState.collectAsState()
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(text = "Include already attempted questions from previous tests?",
                modifier = Modifier.weight(3f))
            Switch(
                checked = testUiState.repeatPreviouslyAttemptedQuestions,
                onCheckedChange = {testScreenViewModel.toggleRepeatPreviouslyAttemptedQuestions()},
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview
@Composable
fun PreviewApp() {
    PreperationAppTheme {
    }
}