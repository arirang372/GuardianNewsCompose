package com.john.guardian.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.john.guardian.AppViewModelProvider
import com.john.guardian.ui.dashboard.NewsSectionHomeScreen
import com.john.guardian.viewmodels.NewsSectionViewModel


@Composable
fun TheGuardianNewsApp(navController: NavHostController = rememberNavController()) {

    val viewModel: NewsSectionViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val sectionState by viewModel.sectionState.collectAsState()

    NewsSectionHomeScreen(
        sectionState = sectionState,
        onTapPressed = {},
        onSectionPressed = {},
        modifier = Modifier)

}