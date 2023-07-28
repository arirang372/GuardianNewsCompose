package com.john.guardian.data

import com.john.guardian.models.Section

data class NewsSectionState(
    val newsSections: Map<NewsType, List<Section>> = emptyMap(),
    val currentNewsType: NewsType = NewsType.Article,
    val currentSelectedSection: Section = Section(id = 0),
    val isShowingHomepage: Boolean = true,
    var uiState: NewsSectionUiState = NewsSectionUiState.Loading
) {
    val currentNewsSection: List<Section> by lazy {
        newsSections[currentNewsType]!!
    }
}


sealed interface NewsSectionUiState {
    data class Success(val news: List<Section>) : NewsSectionUiState

    data class Error(val message: String) : NewsSectionUiState
    object Loading : NewsSectionUiState
}