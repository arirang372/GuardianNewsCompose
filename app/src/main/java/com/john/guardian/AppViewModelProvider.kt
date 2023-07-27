package com.john.guardian

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.john.guardian.viewmodels.GuardianDashboardViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            GuardianDashboardViewModel(
                guardianApplication().container.repository
            )
        }
    }
}

fun CreationExtras.guardianApplication(): TheGuardianNewsApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TheGuardianNewsApplication