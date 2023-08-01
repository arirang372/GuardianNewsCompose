package com.john.guardian.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.guardian.data.GuardianNewsRepository
import com.john.guardian.data.NewsDashboardState
import com.john.guardian.data.NewsDashboardUiState
import com.john.guardian.data.NewsType
import com.john.guardian.models.Section
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsDashboardViewModel(private val repository: GuardianNewsRepository) : ViewModel() {

    private val _dashboardState = MutableStateFlow(NewsDashboardState())
    val dashboardState: StateFlow<NewsDashboardState> = _dashboardState

    init {
        fetchSections()
    }

    fun updateCurrentNewsType(newsType: NewsType) {
        _dashboardState.update {
            it.copy(
                currentNewsType = newsType
            )
        }
    }

    fun resetHomeScreenStates() {
        _dashboardState.update {
            it.copy(
                currentSelectedSection = it.newsSections[it.currentNewsType]?.get(0) ?: Section(id = 0)
            )
        }
    }

    private fun fetchSections() = viewModelScope.launch {
        try {
            val response = repository.getSections().response
            _dashboardState.value =
                _dashboardState.value.copy(
                    newsSections = mapOf(
                        NewsType.Article to response.results,
                        NewsType.LiveBlog to response.results
                    ),
                    uiState = NewsDashboardUiState.Success(response.results)
                )

        } catch (exception: Exception) {
            _dashboardState.value =
                _dashboardState.value.copy(
                    uiState = NewsDashboardUiState.Error(exception.toString())
                )
        }
    }

}