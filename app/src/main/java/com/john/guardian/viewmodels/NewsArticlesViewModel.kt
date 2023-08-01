package com.john.guardian.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.google.gson.Gson
import com.john.guardian.data.GuardianNewsRepository
import com.john.guardian.data.NewsArticlesState
import com.john.guardian.data.NewsArticlesUiState
import com.john.guardian.models.Article
import com.john.guardian.models.Section
import com.john.guardian.ui.article.NewsArticlesDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsArticlesViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: GuardianNewsRepository
) : ViewModel() {
    private var sectionString: String
    private val _articlesState = MutableStateFlow(NewsArticlesState())
    val articlesState: StateFlow<NewsArticlesState> = _articlesState

    init {
        sectionString = checkNotNull(savedStateHandle[NewsArticlesDestination.section])
        val selectedSection: Section = Gson().fromJson(sectionString, Section::class.java)
        val articleType: String =
            checkNotNull(savedStateHandle[NewsArticlesDestination.articleType])
        fetchArticlesWithPagingData(selectedSection, articleType.toLowerCase())
    }

    private fun fetchArticles(selectedSection: Section, articleType: String = "") =
        viewModelScope.launch {
            _articlesState.value =
                _articlesState.value.copy(
                    selectedSection = selectedSection,
                    uiState = NewsArticlesUiState.Loading
                )
            try {
                val result = repository.getArticles(
                    selectedSection.sectionName.orEmpty(),
                    articleType
                )
                selectedSection.articles = result.data.orEmpty()
                _articlesState.value =
                    _articlesState.value.copy(
                        selectedSection = selectedSection,
                        uiState = NewsArticlesUiState.Success(selectedSection.articles.orEmpty())
                    )

            } catch (exception: Exception) {
                _articlesState.value =
                    _articlesState.value.copy(
                        selectedSection = selectedSection,
                        uiState = NewsArticlesUiState.Error(exception.message.toString())
                    )
            }
        }

    private fun fetchArticlesWithPagingData(selectedSection: Section, articleType: String = "") = viewModelScope.launch {
        _articlesState.value =
            _articlesState.value.copy(
                selectedSection = selectedSection,
                uiState = NewsArticlesUiState.Loading
            )
        try {
            val result: Flow<PagingData<Article>> = repository.getSectionNewsArticle(
                selectedSection,
                articleType
            )
            selectedSection.pagerData = result
            _articlesState.value =
                _articlesState.value.copy(
                    selectedSection = selectedSection,
                    uiState = NewsArticlesUiState.Success(selectedSection.articles.orEmpty())
                )

        } catch (exception: Exception) {
            _articlesState.value =
                _articlesState.value.copy(
                    selectedSection = selectedSection,
                    uiState = NewsArticlesUiState.Error(exception.message.toString())
                )
        }
    }
}