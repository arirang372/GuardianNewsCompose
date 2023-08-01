package com.john.guardian.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.guardian.data.GuardianNewsRepository
import com.john.guardian.data.NewsSectionState
import com.john.guardian.data.NewsSectionUiState
import com.john.guardian.data.NewsType
import com.john.guardian.models.Section
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsSectionViewModel(private val repository: GuardianNewsRepository) : ViewModel() {

    private val _sectionState = MutableStateFlow(NewsSectionState())
    val sectionState: StateFlow<NewsSectionState> = _sectionState

    init {
        fetchSections()
    }

    fun updateCurrentNewsType(newsType: NewsType) {
        _sectionState.update {
            it.copy(
                currentNewsType = newsType
            )
        }
    }

    fun resetHomeScreenStates() {
        _sectionState.update {
            it.copy(
                currentSelectedSection = it.newsSections[it.currentNewsType]?.get(0) ?: Section(id = 0)
            )
        }
    }

    private fun fetchSections() = viewModelScope.launch {
        try {
            val response = repository.getSections().response
            _sectionState.value =
                _sectionState.value.copy(
                    newsSections = mapOf(
                        NewsType.Article to response.results,
                        NewsType.LiveBlog to response.results
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