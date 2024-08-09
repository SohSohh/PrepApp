package com.example.myapplication

import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.myapplication.TestScreen.TestingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(modifier: Modifier = Modifier, testScreenViewModel: TestScreenViewModel = viewModel(),
            navController:NavHostController = rememberNavController()) {
    val uiState by testScreenViewModel.uiState.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            if (uiState.showTopBar) {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = "TopBarTitlePlaceholder",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                )
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)) {
            
            NavHost(
                navController = navController,
                startDestination = "TestConfigurationScreen",
                modifier = Modifier
            ) {
                composable(route = "TestConfigurationScreen") {
                    TestConfigurationScreen(
                        modifier = Modifier,
                        onStartButtonClicked = {
                            navController.navigate(route = "TestingScreen")
                            testScreenViewModel.initializeQuestions()
                            testScreenViewModel.disableBars()
                        },
                        testScreenViewModel = testScreenViewModel
                    )
                }
                composable(route = "TestingScreen") {
                    TestingScreen(modifier = Modifier,
                        testScreenViewModel = testScreenViewModel,
                        onEndOfTest = { navController.navigate(route = "EndScreen") },
                        onBackButtonOrGesture = {
                            cancelTestOrReturnToHome(testScreenViewModel, navController)
                            testScreenViewModel.enableBars()
                        })
                }
                composable(route = "EndScreen") {
                    EndOfTestScreen(modifier = Modifier,
                        testScreenViewModel = testScreenViewModel,
                        onBackButtonOrGesture = {
                            cancelTestOrReturnToHome(testScreenViewModel, navController)
                            testScreenViewModel.enableBars()
                        })
                }
            }
        }
    }
}
private fun cancelTestOrReturnToHome(viewModel: TestScreenViewModel, navController: NavHostController) {
    navController.popBackStack("TestConfigurationScreen", false)
    viewModel.reset()
}


@Preview
@Composable
fun AppPreview() {
    PreperationAppTheme {
        MainApp()
    }
}