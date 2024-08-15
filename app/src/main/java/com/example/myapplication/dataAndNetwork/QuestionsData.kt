package com.example.myapplication.dataAndNetwork

enum class subjects() {
    Physics, Mathematics, Chemistry, Biology, English, Intelligence, Computers
}

data class question(
    var question:String,
    var choices:List<String>,
    var answer:String,
    var subject: subjects,
) {
    fun isMatchingSearchQuery(query:String):Boolean {
        return question.contains(query, ignoreCase = true)
    }
    fun isMatchingSubject(query:String):Boolean {
        return subject.toString().contains(query, ignoreCase = true)
    }
    fun isMatchingChoices(query:String):Boolean {
        return choices.any { it.contains(query, ignoreCase = true) }
    }
}

var physicsQ:List<question> = mutableListOf(
    question("Physics test q1", listOf("It shouldn't work, also I forgot to test the multiline feature oops", "Okay, I feel like this might be an issue I overlooked", "C", "D"), "Okay, I feel like this might be an issue I overlooked", subjects.Physics),
    question("Physics test q2", listOf("E", "F", "G", "H"), "H", subjects.Physics),
    question("Physics test q3", listOf("P", "Q", "R", "S"), "Q", subjects.Physics)
)

var dummyTestingQuestions:List<question> = mutableListOf(
    question("dummy (PHY) test q1", listOf("A", "B", "C", "D"), "B", subjects.Physics),
    question("dummy (MATH) test q2", listOf("E", "F", "G", "H"), "H", subjects.Mathematics),
    question("dummy (ENG) q3", listOf("P", "Q", "R", "S"), "Q", subjects.English)
)
var mathsQ:List<question> = mutableListOf(
    question("Mathematics test q1 oh wow im testing the maximum line width that's weird", listOf("1", "2", "3", "4"), "2", subjects.Mathematics),
    question("mathematics test q2", listOf("5", "4", "2", "3"), "4", subjects.Mathematics)
)
var englishQ:List<question> = mutableListOf(
    question("(ENG) q1", listOf("P", "Q", "R", "S"), "Q", subjects.English),
    question("(ENG) q2", listOf("T", "U", "V", "W"), "W", subjects.English),
    question("() q3", listOf("L", "Z", "Y", "X"), "L", subjects.English)
)
var allQuestionsSet:List<List<question>> = mutableListOf( // MAKE SURE IT FOLLOWS THE SAME ORDER AS IN LINE 129 OF VIEWMODEL
    physicsQ, mathsQ, englishQ
)