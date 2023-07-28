package com.john.guardian.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.john.guardian.R
import com.john.guardian.data.NewsSectionState
import com.john.guardian.data.NewsSectionUiState
import com.john.guardian.data.NewsType
import com.john.guardian.models.Section
import com.john.guardian.ui.navigation.NavigationDestination


object DashboardDestination : NavigationDestination {
    override val route = "article"
    override val titleRes = R.string.article
}


@Composable
fun NewsSectionHomeScreen(
    sectionState: NewsSectionState,
    onTapPressed: (NewsType) -> Unit,
    onSectionPressed: (Section) -> Unit,
    modifier: Modifier
) {
    when (sectionState.uiState) {
        is NewsSectionUiState.Loading -> LoadingScreen(
            modifier
                .fillMaxSize()
                .size(200.dp)
        )

        is NewsSectionUiState.Success ->
            NewSectionsScreen(sectionState = sectionState,
            onTapPressed = onTapPressed,
            onSectionPressed = onSectionPressed,
            modifier.fillMaxSize())

        is NewsSectionUiState.Error ->
            ErrorScreen(error = (sectionState.uiState as NewsSectionUiState.Error).message)
    }
}

@Composable
private fun NewSectionsScreen(
    sectionState: NewsSectionState,
    onTapPressed: (NewsType) -> Unit,
    onSectionPressed: (Section) -> Unit,
    modifier: Modifier
) {
    val navItemContentList = listOf(
        NavigationItemContent(
            newsType = NewsType.Article,
            icon = Icons.Default.Inbox,
            text = stringResource(id = R.string.article)
        ),
        NavigationItemContent(
            newsType = NewsType.Live,
            icon = Icons.Default.Send,
            text = stringResource(id = R.string.live)
        )
    )
    if (sectionState.isShowingHomepage) {
        NewsSectionContent(
            sectionState = sectionState,
            onTapPressed = onTapPressed,
            onSectionPressed = onSectionPressed,
            navItemContentList = navItemContentList,
            modifier = modifier
        )
    } else {

    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.progress_bar),
        contentDescription = stringResource(id = R.string.loading),
        modifier = modifier
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, error: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = error)
    }
}


@Composable
private fun NewsSectionContent(
    sectionState: NewsSectionState,
    onTapPressed: (NewsType) -> Unit,
    onSectionPressed: (Section) -> Unit,
    navItemContentList: List<NavigationItemContent>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        NewsSectionContent(
            sectionState = sectionState,
            onSectionPressed = onSectionPressed,
            modifier = Modifier.weight(1f)
        )

        AnimatedVisibility(visible = true) {
            val bottomNavContentDescription = stringResource(id = R.string.navigation_bottom)
            NewsSectionNavigationBar(
                currentTab = sectionState.currentNewsType,
                onTapPressed = onTapPressed,
                navItemContentList = navItemContentList,
                modifier = Modifier.testTag(bottomNavContentDescription)
            )
        }
    }
}


@Composable
fun NewsSectionContent(
    sectionState: NewsSectionState,
    onSectionPressed: (Section) -> Unit,
    modifier: Modifier = Modifier
) {
    val sections: List<Section> = sectionState.newsSections[sectionState.currentNewsType]!!

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            NewsSectionTopBar(
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(sections, key = { section -> section.sectionName!! }) { section ->
            NewsSectionListItem(
                section = section,
                onCardClick = {
                    onSectionPressed(section)
                })
        }
    }
}

@Composable
private fun NewsSectionTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSectionListItem(
    section: Section,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = section.sectionName!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )
        }
    }
}


@Composable
private fun NewsSectionNavigationBar(
    currentTab: NewsType,
    onTapPressed: (NewsType) -> Unit,
    navItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        for (navItem in navItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.newsType,
                onClick = { onTapPressed(navItem.newsType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

private data class NavigationItemContent(
    val newsType: NewsType,
    val icon: ImageVector,
    val text: String
)