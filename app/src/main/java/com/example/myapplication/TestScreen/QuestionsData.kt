package com.example.myapplication.TestScreen

data class question(
    var question:String,
    var choices:List<String>,
    var answer:String
)

var questionsList:List<question> = mutableListOf(
    question("Test question 1", mutableListOf("A", "B", "C", "D"), "D"),
    question("test question 2", mutableListOf("1", "2", "3", "4"), "2"),
    question("test question 3", mutableListOf("E", "F", "G", "H"), "G"),
    question("test question 4", mutableListOf("China", "ER", "Faith", "belief"), "belief")
)