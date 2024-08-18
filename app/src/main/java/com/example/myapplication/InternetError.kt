package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.PreperationAppTheme

@Composable
fun InternetError() {
    var isVisible by remember { mutableStateOf(true) }

    // Trigger the visibility change when the composable is first shown
    LaunchedEffect(Unit) {
        isVisible = true
    }
    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = isVisible,
            enter = fadeIn(animationSpec = tween(250)) + slideInVertically(initialOffsetY = {-it/4}),
            modifier = Modifier) {
            Card(
                modifier = Modifier.padding(horizontal = 30.dp),
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Network error",
                        modifier = Modifier.padding(15.dp),
                        style = MaterialTheme.typography.bodyMedium+ TextStyle(textAlign = TextAlign.Center)
                    )
                    Text(
                        text = "Please check your connection and try again",
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.displayMedium + TextStyle(textAlign = TextAlign.Center)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun InternetErrorPreview() {
    PreperationAppTheme() {
        InternetError()
    }
}