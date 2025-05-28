package com.john.guardian.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.john.guardian.ui.article.NewsArticlesDestination
import com.john.guardian.ui.article.NewsArticlesScreen
import com.john.guardian.ui.article.SingleArticleScreen
import com.john.guardian.ui.article.SingleArticleScreenDestination
import com.john.guardian.ui.article.SingleArticleScreenDestination.body
import com.john.guardian.ui.article.SingleArticleScreenDestination.lastModifiedFormatted
import com.john.guardian.ui.article.SingleArticleScreenDestination.sectionName
import com.john.guardian.ui.article.SingleArticleScreenDestination.thumbnails
import com.john.guardian.ui.article.SingleArticleScreenDestination.trailText
import com.john.guardian.ui.article.SingleArticleScreenDestination.webTitle
import com.john.guardian.ui.section.NewsDashboardScreen
import com.john.guardian.ui.section.NewsDashboardDestination

@Composable
fun TheGuardianNewsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NewsDashboardDestination.route,
        modifier = modifier
    ) {
        composable(
            route = NewsDashboardDestination.route
        ) {
            NewsDashboardScreen(
                navigateToArticles = { section: String, articleType: String ->
                    navController.navigate("${NewsArticlesDestination.route}?${NewsArticlesDestination.section}=${section}&${NewsArticlesDestination.articleType}=${articleType}")
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
            NewsArticlesScreen(
                navigateToArticle = { webTitle: String, trailText: String, lastModifiedFormatted: String, body: String, thumbnails: String, sectionName: String ->
                    navController.navigate("${SingleArticleScreenDestination.route}?webTitle={$webTitle}&trailText={$trailText}&lastModifiedFormatted={$lastModifiedFormatted}&body={$body}&thumbnails={$thumbnails}&sectionName={$sectionName}")
                },
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = SingleArticleScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(webTitle) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(trailText) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(lastModifiedFormatted) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(body) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(thumbnails) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(sectionName) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            SingleArticleScreen(navigateBack = {
                navController.navigateUp()
            })
        }
    }
}