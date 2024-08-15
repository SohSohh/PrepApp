package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Set of Material typography styles to start with

val merriRegular = FontFamily(
    Font(R.font.merriweather_regular),
    Font(R.font.merriweather_bold, FontWeight.Bold),
    Font(R.font.merriweather_light, FontWeight.Light),
)
val quicksand = FontFamily(
    Font(R.font.quicksand_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.quicksand_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.quicksand_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.quicksand_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.quicksand_bold, FontWeight.Bold, FontStyle.Normal),

)

val ztgatha = FontFamily(
    Font(R.font.ztgatha_semibold, FontWeight.SemiBold, FontStyle.Normal)
)

val Typography = Typography(
    titleLarge = TextStyle( // For topbar Heading
        fontFamily = ztgatha,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        fontSize = 40.sp
    ),
    titleMedium = TextStyle( // For medium titled
        fontFamily = quicksand,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Normal,
        fontSize = 35.sp
    ),
    displayMedium = TextStyle( // For configuration / settings
        fontFamily = quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp
    ),
    displaySmall = TextStyle( // for bottom bar elements and buttons
        fontFamily = quicksand,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle( // For choices
        fontFamily = quicksand,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 23.sp
    ),
    bodyMedium = TextStyle( // FOR Questions
        fontFamily = quicksand,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Normal,
        fontSize = 27.sp
    ),
    labelMedium = TextStyle( // For labels and such
        fontFamily = quicksand,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 13.sp
    ),
    headlineMedium = TextStyle( // Search bars etc
        fontFamily = quicksand,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 17.sp
    )
    // Add other text styles as needed
)