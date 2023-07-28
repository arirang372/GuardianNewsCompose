package com.john.guardian.data

import com.john.guardian.models.Article
import com.john.guardian.models.Field
import com.john.guardian.models.Section

data class NewsSectionState(
    val news: Map<NewsType, List<Section>> = emptyMap(),
    val currentNewsType: NewsType = NewsType.Article,
    val currentSelectedArticle: Article =
        Article(
            id = 0,
            isHosted = false,
            fields = Field(showInRelatedContent = false, isLive = false)
        ),
    val isShowingHomepage: Boolean = true,
    var uiState: NewsSectionUiState = NewsSectionUiState.Loading
) {
    val currentNewsSection: List<Section> by lazy {
        news[currentNewsType]!!
    }
}


sealed interface NewsSectionUiState {
    data class Success(val news: List<Section>) : NewsSectionUiState

    data class Error(val message: String) : NewsSectionUiState
    object Loading : NewsSectionUiState
}