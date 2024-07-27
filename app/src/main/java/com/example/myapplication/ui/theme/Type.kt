package com.example.myapplication.ui.theme

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Set of Material typography styles to start with

val merri_regular = FontFamily(
    Font(R.font.merriweather_regular),
    Font(R.font.merriweather_bold, FontWeight.Bold),
    Font(R.font.merriweather_light, FontWeight.Light),
    Font(R.font.merriweather_italic, style = FontStyle.Italic),
    Font(R.font.merriweather_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.merriweather_bolditalic, FontWeight.Bold, FontStyle.Italic)
)
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = merri_regular,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    displayMedium = TextStyle(
        fontFamily = merri_regular,
        fontSize = 20.sp
    )
)