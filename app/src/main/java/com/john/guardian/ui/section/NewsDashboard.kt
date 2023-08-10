package com.john.guardian.ui.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.john.guardian.AppViewModelProvider
import com.john.guardian.R
import com.john.guardian.data.NewsDashboardState
import com.john.guardian.data.NewsDashboardUiState
import com.john.guardian.data.NewsType
import com.john.guardian.models.Section
import com.john.guardian.ui.TheGuardianTopAppBar
import com.john.guardian.ui.navigation.NavigationDestination
import com.john.guardian.viewmodels.NewsDashboardViewModel


object NewsDashboardDestination : NavigationDestination {
    override val route = "sections"
    override val titleRes = R.string.app_name
}

@Composable
fun NewsDashboardScreen(
    navigateToArticles: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsDashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val dashboardState by viewModel.dashboardState.collectAsState()

    val onTapPressed: (NewsType) -> Unit = { newsType ->
        viewModel.updateCurrentNewsType(newsType)
        viewModel.resetHomeScreenStates()
    }
    when (dashboardState.uiState) {
        is NewsDashboardUiState.Loading -> LoadingScreen(
            modifier
                .fillMaxSize()
                .size(200.dp)
        )

        is NewsDashboardUiState.Success ->
            NewsDashboardSuccessScreen(
                dashboardState = dashboardState,
                onTapPressed = onTapPressed,
                onSectionPressed = navigateToArticles,
                modifier.fillMaxSize()
            )

        is NewsDashboardUiState.Error ->
            ErrorScreen(error = (dashboardState.uiState as NewsDashboardUiState.Error).message)
    }
}

@Composable
private fun NewsDashboardSuccessScreen(
    dashboardState: NewsDashboardState,
    onTapPressed: (NewsType) -> Unit,
    onSectionPressed: (String, String) -> Unit,
    modifier: Modifier
) {
    val navItemContentList = listOf(
        NavigationItemContent(
            newsType = NewsType.Article,
            icon = Icons.Default.Inbox,
            text = stringResource(id = R.string.articles_screen)
        ),
        NavigationItemContent(
            newsType = NewsType.LiveBlog,
            icon = Icons.Default.Send,
            text = stringResource(id = R.string.live)
        )
    )

    Scaffold(topBar = {
        TheGuardianTopAppBar(
            title =
            stringResource(
                id =
                NewsDashboardDestination.titleRes
            ), canNavigateBack = false
        )
    }) { innerPadding ->
        NewsDashboardSectionContent(
            dashboardState = dashboardState,
            onTapPressed = onTapPressed,
            onSectionPressed = onSectionPressed,
            navItemContentList = navItemContentList,
            modifier = modifier.padding(innerPadding)
        )
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
private fun NewsDashboardSectionContent(
    dashboardState: NewsDashboardState,
    onTapPressed: (NewsType) -> Unit,
    onSectionPressed: (String, String) -> Unit,
    navItemContentList: List<NavigationItemContent>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        NewsDashboardColumnContents(
            dashboardState = dashboardState,
            onSectionPressed = onSectionPressed,
            modifier = Modifier.weight(1f)
        )

        AnimatedVisibility(visible = true) {
            val bottomNavContentDescription = stringResource(id = R.string.navigation_bottom)
            NewsDashboardNavigationBar(
                currentTab = dashboardState.currentNewsType,
                onTapPressed = onTapPressed,
                navItemContentList = navItemContentList,
                modifier = Modifier.testTag(bottomNavContentDescription)
            )
        }
    }
}


@Composable
fun NewsDashboardColumnContents(
    dashboardState: NewsDashboardState,
    onSectionPressed: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sections: List<Section> = dashboardState.newsSections[dashboardState.currentNewsType]!!

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        items(sections, key = { section -> section.sectionName!! }) { section ->
            NewsSectionListItem(
                section = section,
                onCardClick = {
                    val sectionString = Gson().toJson(section)
                    onSectionPressed(sectionString, dashboardState.currentNewsType.name)
                })
        }
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
private fun NewsDashboardNavigationBar(
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