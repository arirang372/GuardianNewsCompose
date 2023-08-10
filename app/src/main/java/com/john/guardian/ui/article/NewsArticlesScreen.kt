package com.john.guardian.ui.article

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.john.guardian.AppViewModelProvider
import com.john.guardian.R
import com.john.guardian.data.NewsArticlesState
import com.john.guardian.data.NewsArticlesUiState
import com.john.guardian.models.Article
import com.john.guardian.ui.TheGuardianTopAppBar
import com.john.guardian.ui.navigation.NavigationDestination
import com.john.guardian.ui.section.ErrorScreen
import com.john.guardian.ui.section.LoadingScreen
import com.john.guardian.viewmodels.NewsArticlesViewModel
import kotlinx.coroutines.flow.Flow

object NewsArticlesDestination : NavigationDestination {
    override val route = "articles"
    override val titleRes = R.string.list_of_articles
    const val section = "section"
    const val articleType = "articleType"
    val routeWithArgs = "$route?$section={$section}&$articleType={$articleType}"
}

@Composable
fun NewsArticlesScreen(
    navigateToArticle: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsArticlesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val articlesState by viewModel.articlesState.collectAsState()
    when (articlesState.uiState) {
        is NewsArticlesUiState.Loading -> LoadingScreen(
            modifier
                .fillMaxSize()
                .size(200.dp)
        )

        is NewsArticlesUiState.Success ->
            NewsArticlesScreen(
                articlesState = articlesState,
                modifier = modifier,
                onBackPressed = navigateBack,
                onArticlePressed = navigateToArticle
            )

        is NewsArticlesUiState.Error ->
            ErrorScreen(error = (articlesState.uiState as NewsArticlesUiState.Error).message)
    }
}

@Composable
private fun NewsArticlesScreen(
    articlesState: NewsArticlesState,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onArticlePressed: (String) -> Unit
) {

    Scaffold(topBar = {
        TheGuardianTopAppBar(
            title = articlesState.selectedSection.sectionName.orEmpty(),
            canNavigateBack = true,
            navigateUp = onBackPressed
        )
    }) { innerPadding ->
        NewsArticlesContent(
            articlesState = articlesState,
            modifier = modifier.padding(innerPadding),
            onArticlePressed = onArticlePressed
        )
    }
}

@SuppressLint("RememberReturnType")
@Composable
private fun NewsArticlesContent(
    articlesState: NewsArticlesState,
    modifier: Modifier = Modifier,
    onArticlePressed: (String) -> Unit = {}
) {
    val pagerData: Flow<PagingData<Article>> = articlesState.selectedSection.pagerData
    val lazyPagingItems: LazyPagingItems<Article> = pagerData.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
            .testTag(stringResource(id = R.string.articles_screen))
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 24.dp)
    ) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = "Waiting for items to load from the backend",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        items(count = lazyPagingItems.itemCount) { index ->
            val article = lazyPagingItems[index]!!
            NewsArticleListItem(
                article = article,
                onCardClick = {
                    onArticlePressed(Gson().toJson(article))
                }
            )
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsArticleListItem(
    article: Article,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        onClick = onCardClick
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(article.fields!!.thumbnail)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.progress_bar),
                contentDescription = stringResource(id = R.string.article_list_item_image),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.webTitle.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )

            Text(
                text = article.fields.lastModifiedFormatted().orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
