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

var physicsQ:List<question> = mutableListOf(
    question("physics test q1", listOf("A", "B", "C", "D"), "B", subjects.Physics),
    question("physics test q2", listOf("E", "F", "G", "H"), "H", subjects.Physics),
    question("physics test q3", listOf("P", "Q", "R", "S"), "Q", subjects.Physics)
)

var mathsQ:List<question> = mutableListOf(
    question("Mathematics test q1", listOf("1", "2", "3", "4"), "2", subjects.Mathematics),
    question("mathematics test q2", listOf("5", "4", "2", "3"), "4", subjects.Mathematics)
)

var allQuestionsSet:List<List<question>> = mutableListOf(
    physicsQ, mathsQ
)