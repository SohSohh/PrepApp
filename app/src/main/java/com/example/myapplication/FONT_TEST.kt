package com.example.myapplication

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.PreperationAppTheme

@Composable
fun FontTest() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(15.dp)) {
            Text(text = "Placeholder", style = MaterialTheme.typography.titleLarge)
        }
        Text(text = "This is a sample question", style = MaterialTheme.typography.bodyMedium)
        Text(text = "This is a sample choice", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "This is a configuration line", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
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
