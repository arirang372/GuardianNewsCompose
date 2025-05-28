package com.john.guardian.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.john.guardian.models.Article
import com.john.guardian.ui.article.SingleArticleScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SingleArticleViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _webTitle = MutableStateFlow("")
    val webTitle: StateFlow<String> = _webTitle

    private val _trailText = MutableStateFlow("")
    val trailText: StateFlow<String> = _trailText

    private val _lastModifiedFormatted = MutableStateFlow("")
    val lastModifiedFormatted: StateFlow<String> = _lastModifiedFormatted

    private val _body = MutableStateFlow("")
    val body: StateFlow<String> = _body

    private val _thumbnails = MutableStateFlow("")
    val thumbnails: StateFlow<String> = _thumbnails

    private val _sectionName = MutableStateFlow("")
    val sectionName: StateFlow<String> = _sectionName


    init {
        _webTitle.value = checkNotNull(savedStateHandle[SingleArticleScreenDestination.webTitle])

        _trailText.value = checkNotNull(savedStateHandle[SingleArticleScreenDestination.trailText])

        _lastModifiedFormatted.value = checkNotNull(savedStateHandle[SingleArticleScreenDestination.lastModifiedFormatted])

        _body.value = checkNotNull(savedStateHandle[SingleArticleScreenDestination.body])

        _thumbnails.value = checkNotNull(savedStateHandle[SingleArticleScreenDestination.thumbnails])

        _sectionName.value = checkNotNull(savedStateHandle[SingleArticleScreenDestination.sectionName])

    }
}