package com.example.myapplication.mcqPool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dataAndNetwork.allQuestionsSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class SearchViewModel: ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _visibleQuestions = MutableStateFlow(allQuestionsSet.flatten())
    val visibleQuestions = searchText.combine(_visibleQuestions.asStateFlow()) {text, questions ->
        if (text.isBlank()) {
            questions
        } else {
            questions.filter {
                it.isMatchingSearchQuery(text) || it.isMatchingSubject(text) || it.isMatchingChoices(text)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _visibleQuestions.value
    )
    fun onQueryEntered(text:String) {
        _searchText.value = text
    }
}