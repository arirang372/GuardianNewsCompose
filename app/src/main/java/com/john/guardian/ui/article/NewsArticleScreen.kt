package com.john.guardian.ui.article

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.john.guardian.R
import com.john.guardian.data.NewsArticleUiState
import com.john.guardian.data.NewsSectionState
import com.john.guardian.models.Article
import com.john.guardian.ui.section.ErrorScreen
import com.john.guardian.ui.section.LoadingScreen


@Composable
fun NewsArticleScreen(
    sectionState: NewsSectionState,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onArticlePressed: (Article) -> Unit
) {
    when (sectionState.articleUiState) {
        is NewsArticleUiState.Loading -> LoadingScreen(
            modifier
                .fillMaxSize()
                .size(200.dp)
        )

        is NewsArticleUiState.Success ->
            NewsArticlesScreen(
                sectionState = sectionState,
                modifier = modifier,
                onBackPressed = onBackPressed,
                onArticlePressed = onArticlePressed
            )

        is NewsArticleUiState.Error ->
            ErrorScreen(error = sectionState.articleUiState.message)
    }
}

@Composable
private fun NewsArticlesScreen(
    sectionState: NewsSectionState,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onArticlePressed: (Article) -> Unit
) {
    BackHandler {
        onBackPressed()
    }

    LazyColumn(
        modifier = modifier
            .testTag(stringResource(id = R.string.articles_screen))
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 24.dp)
    ) {
        item {
            ArticleScreenTopBar(
                onBackButtonClicked = onBackPressed,
                sectionState = sectionState
            )
        }
        items(sectionState.currentSelectedSection.articles.orEmpty(),
            key = { article -> article.webTitle!! }) { article ->
            NewsArticleListItem(
                article = article,
                onCardClick = { onArticlePressed(article) }
            )
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


@Composable
private fun ArticleScreenTopBar(
    onBackButtonClicked: () -> Unit,
    sectionState: NewsSectionState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackButtonClicked,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = sectionState.currentSelectedSection.sectionName.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}
