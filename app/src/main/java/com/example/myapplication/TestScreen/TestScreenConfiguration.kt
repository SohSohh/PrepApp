package com.example.myapplication.TestScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestConfigurationScreen(modifier:Modifier = Modifier, testScreenViewModel: TestScreenViewModel = viewModel()) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "TopBarTitlePlaceholder", style = MaterialTheme.typography.titleLarge) }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Button(onClick = {
                testScreenViewModel.toggleTest()
            }
            ) {
                Text(text = "Start")
            }
        }

    ) { innerPadding ->
        ConfigurationsList(modifier = Modifier.padding(innerPadding))
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
        //-----------------------
        var text by remember { mutableStateOf("")}
        var error by remember { mutableStateOf(false) }
        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
            OutlinedTextField(value = text,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                shape = MaterialTheme.shapes.medium,
                onValueChange = {text = it},
                label = {
                            if (!error) {
                                Text(text = "Total number of questions", style = MaterialTheme.typography.titleSmall)
                            } else {
                                Text(text = "Please enter a valid value less than 300", style = MaterialTheme.typography.titleSmall)
                            }
                        },
                isError = error,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (text.toInt() > 300 || text.toInt() < 0) {
                            error = true
                        } else {
                            error = false
                            testUiState.totalQuestions = text.toInt()
                        }
                    }
                )


            )
        }
        //------------------------------
    }
}


@Preview
@Composable
fun PreviewApp() {
    PreperationAppTheme {
        TestConfigurationScreen()
    }
}