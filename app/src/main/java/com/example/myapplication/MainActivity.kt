package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.compose.PreperationAppTheme
import com.example.myapplication.TestScreen.TestConfigurationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PreperationAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   TestConfigurationScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

