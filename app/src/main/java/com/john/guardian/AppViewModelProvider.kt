package com.john.guardian

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.john.guardian.viewmodels.NewsSectionViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NewsSectionViewModel(
                guardianApplication().container.repository
            )
        }
    }
}

fun CreationExtras.guardianApplication(): TheGuardianNewsApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TheGuardianNewsApplication