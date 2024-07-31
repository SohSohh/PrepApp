package com.example.myapplication.TestScreen

enum class subjects() {
    Physics, Mathematics, Chemistry, Biology, English, Intelligence, Computers
}

data class question(
    var question:String,
    var choices:List<String>,
    var answer:String,
    var subject: subjects,
)

var questionsList:List<question> = mutableListOf(
    question("Test question 1", mutableListOf("A", "B", "C", "D"), "D", subjects.Physics),
    question("test question 2", mutableListOf("1", "2", "3", "4"), "2", subjects.Biology),
    question("test question 3", mutableListOf("E", "F", "G", "H"), "G", subjects.English),
    question("test question 4", mutableListOf("China", "ER", "Faith", "belief"), "belief", subjects.Mathematics)
)