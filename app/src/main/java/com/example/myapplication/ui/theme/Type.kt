package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Set of Material typography styles to start with

val merriRegular = FontFamily(
    Font(R.font.merriweather_regular),
    Font(R.font.merriweather_bold, FontWeight.Bold),
    Font(R.font.merriweather_light, FontWeight.Light),
)
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = merriRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    displayMedium = TextStyle(
        fontFamily = merriRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = merriRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp
    ),
    titleSmall = TextStyle(
        fontFamily = merriRegular,
        fontSize = 15.sp
    )
    // Add other text styles as needed
)