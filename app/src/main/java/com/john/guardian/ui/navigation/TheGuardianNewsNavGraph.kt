package com.john.guardian.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.john.guardian.ui.article.NewsArticlesDestination
import com.john.guardian.ui.article.NewsArticleScreen
import com.john.guardian.ui.section.NewsSectionDestination
import com.john.guardian.ui.section.NewsSectionHomeScreen


@Composable
fun TheGuardianNewsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NewsSectionDestination.route,
        modifier = modifier
    ) {
        composable(
            route = NewsSectionDestination.route
        ) {
            NewsSectionHomeScreen(
                navigateToArticles = { section: String, articleType: String ->
                    navController.navigate("${NewsArticlesDestination.route}?section=${section}&articleType=${articleType}")
                }
            )
        }

        composable(
            route = NewsArticlesDestination.routeWithArgs,
            arguments = listOf(
                navArgument(NewsArticlesDestination.section) {
                    type = NavType.StringType
                    defaultValue = ""
                }, navArgument(NewsArticlesDestination.articleType) {
                    type = NavType.StringType
                    defaultValue = ""
                })
        ) {
            NewsArticleScreen(
                navigateToArticle = {

                },
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}