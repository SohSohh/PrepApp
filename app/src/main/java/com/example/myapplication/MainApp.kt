package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreen.EndOfTestScreen
import com.example.myapplication.TestScreen.TestConfigurationScreen
import com.example.myapplication.TestScreen.TestingScreen
import com.example.myapplication.mcqPool.McqPoolScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(modifier: Modifier = Modifier,
            navController:NavHostController = rememberNavController()) {
    val testScreenViewModel = remember {TestScreenViewModel()}
    val uiState by testScreenViewModel.uiState.collectAsState()
    val showBar = uiState.showBars

        Scaffold(
            modifier = modifier,
            topBar = {
                if (showBar) {
                        CenterAlignedTopAppBar(title = {
                            Text(
                                text = "TopBarTitlePlaceholder",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        )
                    }
            },
            bottomBar = {
                if (showBar) {
                    BottomAppBar(
                        actions = {
                            BottomNavButton(
                                text = "Test",
                                icon = Icons.Filled.Edit,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate(route = "TestConfigurationScreen") }
                            )
                            BottomNavButton(
                                text = "Question Pool",
                                icon = Icons.Filled.DateRange,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                        navController.navigate(route = "Pool") //{
//                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
//                                            launchSingleTop = true }
                                })
                        }
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "TestConfigurationScreen",
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() },
                ) {
                    composable(route = "TestConfigurationScreen") {
                        TestConfigurationScreen(
                            modifier = Modifier.fillMaxSize(),
                            onStartButtonClicked = {
                                navController.navigate(route = "TestingScreen")
                                testScreenViewModel.initializeQuestions()
                                testScreenViewModel.disableBars()
                            },
                            testScreenViewModel = testScreenViewModel,
                        )
                    }
                    composable(route = "TestingScreen") {
                        TestingScreen(modifier = Modifier.fillMaxSize(),
                            testScreenViewModel = testScreenViewModel,
                            onEndOfTest = { navController.navigate(route = "EndScreen") },
                            navController = navController)
                    }
                    composable(route = "EndScreen") {
                        EndOfTestScreen(modifier = Modifier.fillMaxSize(),
                            testScreenViewModel = testScreenViewModel,
                            onBackButtonOrGesture = {
                                cancelTestOrReturnToHome(testScreenViewModel, navController)
                                testScreenViewModel.enableBars()
                            })
                    }
                    composable(route = "Pool") {
                        McqPoolScreen(testScreenViewModel = testScreenViewModel,
                            modifier = Modifier.fillMaxSize())
                    }
                }
                 }
            }
        }
private fun cancelTestOrReturnToHome(viewModel: TestScreenViewModel, navController: NavHostController) {
    navController.popBackStack("TestConfigurationScreen", false)
    viewModel.reset()
}
@Composable
fun BottomNavButton(
    modifier:Modifier = Modifier,
    text:String,
    icon:ImageVector,
    onClick:() -> Unit,
) {
    Button(onClick = remember { onClick },
        modifier = modifier.padding(horizontal = 5.dp),
        shape = MaterialTheme.shapes.medium,
        interactionSource = NoRippleInteractionSource(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray,
            contentColor = Color.Black)
    ) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null)
            Text(text = text)
        }
    }
}
class NoRippleInteractionSource() : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

@Preview
@Composable
fun AppPreview() {
    PreperationAppTheme {
        MainApp()
    }
}