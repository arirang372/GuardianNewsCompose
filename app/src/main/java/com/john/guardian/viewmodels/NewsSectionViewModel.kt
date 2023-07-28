package com.john.guardian.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.guardian.data.GuardianNewsRepository
import com.john.guardian.data.NewsSectionState
import com.john.guardian.data.NewsSectionUiState
import com.john.guardian.data.NewsType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsSectionViewModel(private val repository: GuardianNewsRepository) : ViewModel() {

    private val _sectionState = MutableStateFlow(NewsSectionState())
    val sectionState: StateFlow<NewsSectionState> = _sectionState

    init {
        fetchSections()
    }

    private fun fetchSections() = viewModelScope.launch {

        try {
            val response = repository.getSections().response
            _sectionState.value =
                _sectionState.value.copy(
                    news = mapOf(
                        NewsType.Article to response.results,
                        NewsType.Live to response.results
                    ),
                    uiState = NewsSectionUiState.Success(response.results)
                )

        } catch (exception: Exception) {
            _sectionState.value =
                _sectionState.value.copy(
                    uiState = NewsSectionUiState.Error(exception.toString())
                )
        }
    }
}