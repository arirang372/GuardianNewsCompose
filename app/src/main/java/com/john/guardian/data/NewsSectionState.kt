package com.john.guardian.data

import com.john.guardian.models.Article
import com.john.guardian.models.Section

data class NewsSectionState(
    val newsSections: Map<NewsType, List<Section>> = emptyMap(),
    val currentNewsType: NewsType = NewsType.Article,
    val currentSelectedSection: Section = Section(id = 0),
    val isShowingHomepage: Boolean = true,
    val uiState: NewsSectionUiState = NewsSectionUiState.Loading,
    val articleUiState: NewsArticleUiState = NewsArticleUiState.Loading
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


sealed interface NewsArticleUiState {

    data class Success(val articles: List<Article>) : NewsArticleUiState

    data class Error(val message: String) : NewsArticleUiState

    object Loading : NewsArticleUiState
}