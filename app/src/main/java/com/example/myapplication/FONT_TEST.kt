package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.PreperationAppTheme
import com.example.myapplication.dataAndNetwork.Api
import com.example.myapplication.dataAndNetwork.question
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FontTest() {
    var requestText by remember { mutableStateOf("Loading...") }
    val scope = rememberCoroutineScope()
    var questionList by remember { mutableStateOf(emptyList<question>()) }
    val testScreenViewModel:TestScreenViewModel = viewModel()
    val uiState by testScreenViewModel.uiState.collectAsState()

// Use LaunchedEffect to start the coroutine when the composable is first launched
    LaunchedEffect(Unit) {
        Api.getQuestions(0,0,0,0,0,0,1).enqueue(object : Callback<List<question>> {
            override fun onResponse(call: Call<List<question>>, response: Response<List<question>>) {
                if (response.isSuccessful) {
                    questionList = response.body() ?: emptyList()
                    requestText = if (questionList.isNotEmpty()) { questionList.toString() } else "No Data"
                } else {
                    requestText = "Error: ${response.code()} - ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<question>>, t: Throwable) {
                requestText = "Failed to load question: ${t.message}"
            }
        })
    }

    val limitList = uiState.limitList
    LaunchedEffect(key1 = Unit) {
        testScreenViewModel.getLimits()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)) {
            Text(text = "Placeholder", style = MaterialTheme.typography.titleLarge)
        }
        Text(text = "This is a sample question", style = MaterialTheme.typography.bodyMedium)
        Text(text = "This is a sample choice", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "This is a configuration line", style = MaterialTheme.typography.displayMedium)
        Text(text = requestText, style = MaterialTheme.typography.displayMedium)
        Text(text = "---------------")
//        Text(text = physicsQ[0].toString(), style = MaterialTheme.typography.displayMedium)
//        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "Pool", style = MaterialTheme.typography.displaySmall)
            Text(text = "Test", style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
@Preview
fun FontTestPreview() {
    PreperationAppTheme {
        FontTest()
    }
}
