package com.john.guardian.data

import com.john.guardian.models.Article
import com.john.guardian.models.Section

data class NewsArticlesState(
    val selectedSection: Section = Section(id = 0),
    val uiState: NewsArticlesUiState = NewsArticlesUiState.Loading
)

sealed interface NewsArticlesUiState {

    data class Success(val articles: List<Article>) : NewsArticlesUiState

    data class Error(val message: String) : NewsArticlesUiState

    object Loading : NewsArticlesUiState

}