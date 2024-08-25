package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.myapplication.dataAndNetwork.fetchAndStoreQuestions
import com.example.myapplication.mcqPool.McqPoolScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(modifier: Modifier = Modifier) {
    val testScreenViewModel = remember {TestScreenViewModel()}
    val navController = rememberNavController()
    val uiState by testScreenViewModel.uiState.collectAsState()
    val showBar = uiState.showBars
    val currentS = uiState.currentScreen
        Scaffold(
            modifier = modifier,
            topBar = {
                if (showBar) {
                    Surface(shadowElevation = 15.dp) {
                        CenterAlignedTopAppBar(title = {
                            Text(
                                text = "PREP",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        )
                    }
                }
            },
            bottomBar = {
                if (showBar) {
                        BottomAppBar(
                            tonalElevation = 15.dp,
                            containerColor = MaterialTheme.colorScheme.surface,
                            actions = {
                                BottomNavButton(
                                    route = "TestConfigurationScreen",
                                    text = "Test",
                                    currentS = currentS,
                                    icon = Icons.Filled.Edit,
                                    modifier = Modifier.weight(1f),
                                    onClick = remember {
                                        {
                                            navController.navigate(route = "TestConfigurationScreen") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                            testScreenViewModel.setScreen("TestConfigurationScreen")
                                        }
                                    }
                                )
                                BottomNavButton(
                                    route = "Pool",
                                    currentS = currentS,
                                    text = "Pool ",
                                    icon = Icons.Filled.DateRange,
                                    modifier = Modifier.weight(1f),
                                    onClick = remember {
                                        {
                                            navController.navigate(route = "Pool") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                            testScreenViewModel.setScreen("Pool")
                                        }
                                    })
                            }
                        )
                }
            }
        ) { innerPadding ->
            val scope = rememberCoroutineScope()
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
                                scope.launch {
                                    navController.navigate(route = "TestingScreen")
                                    testScreenViewModel.initializeQuestions()
                                    testScreenViewModel.disableBars()
                                    delay(750)
                                }

                            },
                            testScreenViewModel = testScreenViewModel,
                        )
                    }
                    composable(route = "TestingScreen") {
                        TestingScreen(
                            testScreenViewModel = testScreenViewModel,
                            onEndOfTest = { navController.navigate(route = "EndScreen") },
                            navController = navController)
                    }
                    composable(route = "EndScreen") {
                        EndOfTestScreen(//modifier = Modifier.fillMaxSize(),
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
    route:String,
    modifier:Modifier = Modifier,
    text:String,
    icon:ImageVector,
    onClick:() -> Unit,
    currentS:String,
) {
    val animatedColor by animateColorAsState(if (currentS == route) {MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.75f)} else {Color.Transparent}, animationSpec = tween(350))
    Button(onClick = onClick,
        modifier = modifier.padding(horizontal = 5.dp),
        shape = MaterialTheme.shapes.medium,
        interactionSource = NoRippleInteractionSource(),
        colors = ButtonDefaults.buttonColors(containerColor = animatedColor,
            contentColor = Color.Black)
    ) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = text, style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    PreperationAppTheme(darkTheme = true) {
        MainApp()
    }
}

@Preview
@Composable
fun AppDPreview() {
    PreperationAppTheme(darkTheme = false) {
        MainApp()
    }
}