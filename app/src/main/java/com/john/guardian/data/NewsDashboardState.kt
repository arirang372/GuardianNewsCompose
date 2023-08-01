package com.john.guardian.data

import com.john.guardian.models.Section

data class NewsDashboardState(
    val newsSections: Map<NewsType, List<Section>> = emptyMap(),
    val currentNewsType: NewsType = NewsType.Article,
    val currentSelectedSection: Section = Section(id = 0),
    val isShowingHomepage: Boolean = true,
    val uiState: NewsDashboardUiState = NewsDashboardUiState.Loading
)

sealed interface NewsDashboardUiState {
    data class Success(val news: List<Section>) : NewsDashboardUiState

    data class Error(val message: String) : NewsDashboardUiState
    object Loading : NewsDashboardUiState
}
