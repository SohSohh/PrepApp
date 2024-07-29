package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreen.EndOfTestScreen
import com.example.myapplication.TestScreen.TestConfigurationScreen
import com.example.myapplication.TestScreen.TestScreenViewModel
import com.example.myapplication.TestScreen.TestingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(modifier: Modifier = Modifier, testScreenViewModel: TestScreenViewModel = viewModel(),
            navController:NavHostController = rememberNavController()) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "TopBarTitlePlaceholder",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "TestConfigurationScreen",
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = "TestConfigurationScreen") {
                TestConfigurationScreen(modifier = Modifier, onStartButtonClicked = { navController.navigate(route = "TestingScreen") }, testScreenViewModel = testScreenViewModel)
            }
            composable(route = "TestingScreen") {
                TestingScreen(modifier = Modifier, testScreenViewModel = testScreenViewModel)
            }
            composable(route = "EndScreen") {
                EndOfTestScreen(modifier = Modifier)
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    PreperationAppTheme {
        MainApp()
    }
}