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
    private var articleString: String
    private val _article = MutableStateFlow(Article(id = 0, isHosted = true))
    val article: StateFlow<Article> = _article

    init {
        articleString = checkNotNull(savedStateHandle[SingleArticleScreenDestination.articleObj])
        val selectedArticle: Article = Gson().fromJson(articleString, Article::class.java)
        _article.value = selectedArticle
    }
}