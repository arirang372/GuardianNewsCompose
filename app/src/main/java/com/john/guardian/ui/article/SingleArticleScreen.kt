package com.john.guardian.ui.article

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.john.guardian.AppViewModelProvider
import com.john.guardian.COLLAPSED_TOP_BAR_HEIGHT
import com.john.guardian.EXPANDED_TOP_BAR_HEIGHT
import com.john.guardian.R
import com.john.guardian.models.Article
import com.john.guardian.ui.navigation.NavigationDestination
import com.john.guardian.viewmodels.SingleArticleViewModel

object SingleArticleScreenDestination : NavigationDestination {
    override val route: String = "article"
    override val titleRes = R.string.single_article
    const val articleObj = "articleObject"
    val routeWithArgs = "$route?articleObject={$articleObj}"
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleArticleScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SingleArticleViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val selectedArticle by viewModel.article.collectAsState()
    val listState = rememberLazyListState()

    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Scaffold(
        topBar = {
            CollapedTopBar(
                article = selectedArticle,
                navigateBack = navigateBack,
                modifier = modifier,
                isCollapsed = isCollapsed
            )
        }
    ) { padding ->
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyColumn(
                modifier = Modifier.padding(padding),
                state = listState
            ) {
                item {
                    ExpandedTopBar(
                        article = selectedArticle,
                        navigateBack = navigateBack,
                        modifier
                    )
                }
                item {
                    SingleArticleContent(selectedArticle, modifier)
                }
            }
        }
    }
}

@Composable
private fun SingleArticleContent(
    article: Article,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = article.webTitle.orEmpty(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = modifier.padding(start = 8.dp, top = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = article.fields!!.trailText.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(start = 8.dp, top = 8.dp)
        )

        Text(
            text = "Updated : ${article.fields!!.lastModifiedFormatted().orEmpty()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(start = 8.dp, bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadData(
                    article.fields.body.orEmpty(),
                    "text/html; charset=utf-8",
                    "UTF-8"
                )
            }
        })

    }
}


@Composable
private fun CollapedTopBar(
    article: Article,
    navigateBack: () -> Unit,
    modifier: Modifier,
    isCollapsed: Boolean
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
    )
    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .height(COLLAPSED_TOP_BAR_HEIGHT)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        AnimatedVisibility(visible = isCollapsed) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigateBack,
                    modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
                Text(text = article.sectionNameChild.orEmpty())
            }
        }
    }
}

@Composable
private fun ExpandedTopBar(
    article: Article,
    navigateBack: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .height(EXPANDED_TOP_BAR_HEIGHT - COLLAPSED_TOP_BAR_HEIGHT),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(article.fields!!.thumbnail)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.progress_bar),
            contentDescription = stringResource(id = R.string.article_list_item_image),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color.White
                )
            }
            Text(
                modifier = modifier.padding(start = 16.dp, bottom = 16.dp),
                text = article.sectionNameChild.orEmpty(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
}


