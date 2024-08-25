package com.example.myapplication.dataAndNetwork

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.myapplication.TestScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

var allQuestionsSet: List<List<question>> = emptyList()

suspend fun fetchAndStoreQuestions() {
    withContext(Dispatchers.IO) {
        try {
            // Fetch questions from API
            val physicsQ = Api.getQuestions(phy = -1).awaitResponse()
            val mathsQ = Api.getQuestions(math = -1).awaitResponse()
            val englishQ = Api.getQuestions(eng = -1).awaitResponse()
            val biologyQ = Api.getQuestions(bio = -1).awaitResponse()
            val intelligenceQ = Api.getQuestions(intel = -1).awaitResponse()
            val chemistryQ = Api.getQuestions(chem = -1).awaitResponse()
            val computerQ = Api.getQuestions(comp = -1).awaitResponse()
            allQuestionsSet = mutableListOf(physicsQ,
                mathsQ,
                englishQ,
                intelligenceQ,
                computerQ,
                chemistryQ,
                biologyQ)
        } catch (e: Exception) {
            // Handle exceptions
            println("Error fetching data: ${e.message}")
        }
    }
}
suspend fun <T> Call<T>.awaitResponse(): T {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body()!!)
                } else {
                    continuation.resumeWithException(Exception("API call failed with error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })

        continuation.invokeOnCancellation {
            try {
                cancel()
            } catch (e: Exception) {
                // Ignore cancellation exceptions
            }
        }
    }
}