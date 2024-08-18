package com.example.myapplication.TestScreen

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color.alpha
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreenUiState
import com.example.myapplication.TestScreenViewModel
import com.example.myapplication.dataAndNetwork.allQuestionsSet
import com.example.myapplication.dataAndNetwork.englishQ
import com.example.myapplication.dataAndNetwork.mathsQ
import com.example.myapplication.dataAndNetwork.physicsQ
import com.example.myapplication.dataAndNetwork.subjects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestConfigurationScreen(
    modifier: Modifier = Modifier,
    testScreenViewModel: TestScreenViewModel = viewModel(),
    onStartButtonClicked: () -> Unit = {}
) {
    val testScreenUiState by testScreenViewModel.uiState.collectAsState()
    val totalQuestions = testScreenUiState.Physics + testScreenUiState.Mathematics + testScreenUiState.Chemistry + testScreenUiState.English + testScreenUiState.Biology + testScreenUiState.Computers + testScreenUiState.Intelligence
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())) {
        Box(
            modifier = modifier
        ) {
            ConfigurationsList(modifier = Modifier.padding(10.dp),
                testScreenUiState = testScreenUiState,
                testScreenViewModel = testScreenViewModel,
                totalQuestions = totalQuestions)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            NavigationButton(text = "Start",
                onClick = onStartButtonClicked,
                modifier = Modifier.padding(15.dp),
                enabledCondition = (totalQuestions <= allQuestionsSet.flatten().size && testScreenUiState.Totaltime != 0))
        }  // THE PREVIEW MIGHT BE SHOWING THE NAV BUTTON SQUISHED BUT THE MAIN APP ACCOMMODATES SCROLL SPACE
        // FOR IT SO DON'T WORRY
    }
}

@Composable
fun NavigationButton(modifier: Modifier = Modifier, text:String, onClick: () -> Unit = {}, enabledCondition:Boolean = true) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(visible = enabledCondition,
        enter = fadeIn( animationSpec = tween(500) )+ slideInVertically(
            initialOffsetY =  { -it / 4 } // Slide in from a little bit above
        ),
        exit = fadeOut( animationSpec = tween(500)) + slideOutVertically(
            targetOffsetY = { -it / 4 } // Slide out to a little bit above
        ),
    )  {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabledCondition,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary, )
        ) {
            Text(text = text, style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ConfigurationsList(modifier:Modifier = Modifier,
                       testScreenViewModel: TestScreenViewModel = viewModel(),
                       testScreenUiState: TestScreenUiState,
                       totalQuestions: Int) {
    Column(modifier = modifier) {
//        // This might be needed to be implemented later
//        TextWithSwitch(text = "Include attempted questions from previous tests",
//            checkedState = testScreenUiState.repeatPreviouslyAttemptedQuestions,
//            onCheckChange = { testScreenViewModel.toggleRepeatPreviouslyAttemptedQuestions() })
//        Text(text = testScreenUiState.repeatPreviouslyAttemptedQuestions.toString())
//        //------
//        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
//        //---------
//        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //------
        TextWithSwitch(text = "Allow backtracking",
            checkedState = testScreenUiState.Backtracking,
            onCheckChange = testScreenViewModel.toggleBacktracking
            )
        //-----
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----
        TextWithSwitch(text = "Allow skipping",
            checkedState = testScreenUiState.AllowSkipping,
            onCheckChange = testScreenViewModel.toggleSkipping)
        //-----------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //----------
        TextWithSwitch(text = "Show correct answers",
            checkedState = testScreenUiState.ShowCorrectAndIncorrect,
            onCheckChange = testScreenViewModel.toggleShowCorrectAndIncorrect)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total Physics questions",
            testScreenUiState = testScreenUiState,
            type = subjects.Physics,
            testScreenViewModel = testScreenViewModel,
            questionLimit = physicsQ.size)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total Mathematics questions",
            testScreenUiState = testScreenUiState,
            type = subjects.Mathematics,
            testScreenViewModel = testScreenViewModel,
            questionLimit = mathsQ.size)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total Chemistry questions",
            testScreenUiState = testScreenUiState,
            type = subjects.Chemistry,
            testScreenViewModel = testScreenViewModel)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total English questions",
            testScreenUiState = testScreenUiState,
            type = subjects.English,
            testScreenViewModel = testScreenViewModel,
            questionLimit = englishQ.size)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total Biology questions",
            testScreenUiState = testScreenUiState,
            type = subjects.Biology,
            testScreenViewModel = testScreenViewModel)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total Computers questions",
            testScreenUiState = testScreenUiState,
            type = subjects.Computers,
            testScreenViewModel = testScreenViewModel)
        //-------------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //-----------
        TextWithTextField(text = "Total Intelligence questions",
            testScreenUiState = testScreenUiState,
            type = subjects.Intelligence,
            testScreenViewModel = testScreenViewModel)
        //--------
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.5f.dp))
        //------------
        TimerInput(testScreenUiState = testScreenUiState,
            totalQuestions = totalQuestions,
            testScreenViewModel = testScreenViewModel)


        //---TESTING----//
//        Text(text = testScreenUiState.Backtracking.toString())
//        Text(text = testScreenUiState.AllowSkipping.toString())
//        Text(text = testScreenUiState.ShowCorrectAndIncorrect.toString())
//        Text(text = testScreenUiState.Physics.toString())
//        Text(text = testScreenUiState.Mathematics.toString())
//        Text(text = testScreenUiState.Chemistry.toString())
//        Text(text = testScreenUiState.English.toString())
//        Text(text = testScreenUiState.Biology.toString())
//        Text(text = testScreenUiState.questions.toString())
    }
}


@Composable
fun TextWithSwitch(modifier:Modifier = Modifier, text:String, checkedState:Boolean, onCheckChange:() -> Unit) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.secondaryContainer
        )) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(5f),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            val onCheckChangedAdapter: (Boolean) -> Unit = {
                onCheckChange()
            }
            Switch(
                checked = checkedState,
                onCheckedChange = onCheckChangedAdapter,
                modifier = Modifier.weight(1f),
                colors = SwitchDefaults.colors( checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledCheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledUncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    disabledCheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    disabledUncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerInput(modifier: Modifier = Modifier,
               testScreenUiState: TestScreenUiState,
               totalQuestions:Int,
               testScreenViewModel: TestScreenViewModel) {
    val time by remember { mutableStateOf(testScreenUiState.Totaltime) }
    var inputValue by remember {
        mutableStateOf(
            "${
                (time / 3600).toString().padStart(2, '0')
            }:${((time % 3600) / 60).toString().padStart(2, '0')}:${
                ((time % 3660) % 60).toString().padStart(2, '0')
            }"
        )
    }
    var absoluteTime by remember { mutableStateOf(0) }
    var limitError by remember { mutableStateOf(false) }
    fun sanitizeInput(input: String): String {
        val digits = input.filter { it.isDigit() }

        // Get hours, minutes, and seconds, pad with zeros as necessary
        val hours = digits.take(2).padEnd(2, '0')
        val minutes = digits.drop(2).take(2).padEnd(2, '0')
        val seconds = digits.drop(4).take(2).padEnd(2, '0')
        absoluteTime = hours.toInt() * 3600 + minutes.toInt() * 60 + seconds.toInt()
        return "$hours:$minutes:$seconds"
    }

    val interactionSource = remember { MutableInteractionSource() }
    var minutesText = if ((testScreenUiState.Totaltime / 60) / totalQuestions == 1) {
        "minute"
    } else {
        "minutes"
    }
    Card(modifier = modifier, colors = CardColors(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.surfaceVariant,MaterialTheme.colorScheme.onSurfaceVariant)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Duration",
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            BasicTextField(
                value = inputValue,
                onValueChange = remember {
                    { newValue ->
                        // Ensure the format is always MM:SS
                        val sanitizedInput = sanitizeInput(newValue)
                        inputValue = sanitizedInput
                    }
                },
                modifier = modifier
                    .padding(vertical = 3.dp)
                    .requiredWidth(120.dp)
                    .align(Alignment.CenterVertically),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = remember {
                    {
                        if (absoluteTime > 0) {
                            limitError = false
                        } else {
                            limitError = true
                        }
                        testScreenViewModel.assignTimer(absoluteTime)
                    }
                }),
                singleLine = true,
                interactionSource = interactionSource,
                textStyle = (TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ) + MaterialTheme.typography.displayMedium)
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = inputValue,
                    innerTextField = innerTextField,
                    supportingText = {
                        Text(
                            text = "${(testScreenUiState.Totaltime / 60) / totalQuestions} ${minutesText} per question" ,
                            style = MaterialTheme.typography.labelMedium + TextStyle(textAlign = TextAlign.Center),
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxWidth(),
                        )
                    },
                    enabled = true,
                    singleLine = true,
                    isError = limitError,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                        focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        unfocusedTextColor = Color.White
                    ),
                    contentPadding = PaddingValues(5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextWithTextField(modifier:Modifier = Modifier,
                      text:String,
                      testScreenUiState: TestScreenUiState,
                      type: subjects,
                      testScreenViewModel: TestScreenViewModel,
                      questionLimit:Int = 0) {
    val subject = when (type) {
        subjects.Physics -> testScreenUiState.Physics
        subjects.Mathematics -> testScreenUiState.Mathematics
        subjects.Chemistry -> testScreenUiState.Chemistry
        subjects.Biology -> testScreenUiState.Biology
        subjects.English -> testScreenUiState.English
        subjects.Intelligence -> testScreenUiState.Intelligence
        subjects.Computers -> testScreenUiState.Computers
    }
    var inputValue by remember { mutableStateOf(subject.toString()) }
    val currentKeyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var limitError by remember { mutableStateOf(false) }
    val onValChangeAdapter: (String) -> Unit = remember {
        {
            if (it.length <= questionLimit) {
                inputValue = it
            }
        }
    }
    val onDoneAdapter: KeyboardActionScope.() -> Unit = remember {
        {
            if (inputValue.toInt() > questionLimit) {
                limitError = true
            } else {
                limitError = false
            }
            testScreenViewModel.assignQuestionsTo(type, inputValue.toInt())
            currentKeyboard?.hide() // Hide the keyboard
            focusManager.clearFocus() // Exit editing mode
        }
    }
    val interactionSource = remember { MutableInteractionSource() }
    Card(modifier = modifier, colors = CardColors(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.surfaceVariant,MaterialTheme.colorScheme.onSurfaceVariant)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, modifier = Modifier, style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(modifier = Modifier.weight(1f))
            BasicTextField(
                value = inputValue,
                onValueChange = onValChangeAdapter,
                modifier = modifier
                    .padding(vertical = 3.dp)
                    .requiredWidth(80.dp)
                    .align(Alignment.CenterVertically),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = onDoneAdapter),
                singleLine = true,
                interactionSource = interactionSource,
                textStyle = (TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ) + MaterialTheme.typography.displayMedium)
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = inputValue,
                    innerTextField = innerTextField,
                    supportingText = {
                        Text(
                            text = "Limit: ${questionLimit}",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxWidth()
                        )
                    },
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    shape = RoundedCornerShape(15.dp),
                    isError = limitError,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                        focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                    ),
                    contentPadding = PaddingValues(5.dp)
                )
            }
        }
//    modifier = modifier
//        .weight(1f)
//        .requiredWidth(75.dp)
//        .align(Alignment.CenterVertically), // fix this issue of test not fitting
//    value = inputValue,
//    onValueChange = onValChangeAdapter,
//    keyboardOptions = KeyboardOptions(
//        keyboardType = KeyboardType.Number,
//        imeAction = ImeAction.Done
//    ),
//    keyboardActions = KeyboardActions(onDone = onDoneAdapter),
//    colors = TextFieldDefaults.colors(
//        unfocusedIndicatorColor = Color.Transparent,
//        focusedIndicatorColor = Color.Transparent,
//        errorIndicatorColor = Color.Transparent,
//        errorTextColor = MaterialTheme.colorScheme.error,
//    ),
//    isError = limitError,
//    singleLine = true,
//    shape = RoundedCornerShape(15.dp),
//    textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp, lineHeight = 15.sp) + MaterialTheme.typography.displayMedium,

    }
}
@Preview
@Composable
fun PreviewLightApp() {
    PreperationAppTheme {
        TestConfigurationScreen()
    }
}

@Preview
@Composable
fun PreviewDarkApp() {
    PreperationAppTheme(darkTheme = true) {
        TestConfigurationScreen()
    }
}