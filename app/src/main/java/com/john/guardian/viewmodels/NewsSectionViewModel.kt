package com.john.guardian.viewmodels

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.john.guardian.data.GuardianNewsRepository
import com.john.guardian.data.NewsArticleUiState
import com.john.guardian.data.NewsSectionState
import com.john.guardian.data.NewsSectionUiState
import com.john.guardian.data.NewsType
import com.john.guardian.models.Article
import com.john.guardian.models.Section
import kotlinx.coroutines.flow.Flow
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

    fun updateCurrentSelectedSection(section: Section) {
        _sectionState.update {
            it.copy(
                currentSelectedSection = section,
                isShowingHomepage = false
            )
        }
        fetchArticles(section, _sectionState.value.currentNewsType.name.toLowerCase())
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

    private fun fetchArticles(selectedSection : Section, articleType: String = "") = viewModelScope.launch {
        _sectionState.value =
            _sectionState.value.copy(
                articleUiState = NewsArticleUiState.Loading
            )
        try {
            val result = repository.getArticles(selectedSection.sectionName.orEmpty(), articleType)
            selectedSection.articles = result.data.orEmpty()
            _sectionState.value =
                _sectionState.value.copy(
                    articleUiState = NewsArticleUiState.Success(result.data.orEmpty())
                )

        } catch (exception: Exception) {
            _sectionState.value =
                _sectionState.value.copy(
                    articleUiState = NewsArticleUiState.Error(exception.toString())
                )
        }
    }
}